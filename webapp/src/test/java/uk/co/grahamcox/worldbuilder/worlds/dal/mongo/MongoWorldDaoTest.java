package uk.co.grahamcox.worldbuilder.worlds.dal.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.assertj.core.api.Assertions;
import org.bson.Document;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import uk.co.grahamcox.worldbuilder.model.IdDetails;
import uk.co.grahamcox.worldbuilder.mongo.EmbeddedMongoRule;
import uk.co.grahamcox.worldbuilder.worlds.model.World;
import uk.co.grahamcox.worldbuilder.worlds.model.WorldId;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

/**
 * Unit Tests for the MongoWorldDao
 */
public class MongoWorldDaoTest {
    /** The current time */
    private static final ZonedDateTime NOW = ZonedDateTime.of(2015, 7, 15, 13, 11, 0, 0, ZoneId.of("UTC"));

    /** JUnit rule to start a MongoDB Service to use */
    @Rule
    public EmbeddedMongoRule embeddedMongoRule = new EmbeddedMongoRule();

    /** The Worlds collection */
    private MongoCollection<Document> worldsCollection;

    /** The DAO to test */
    private MongoWorldDao dao;

    /**
     * Set up the DAO to test
     */
    @Before
    public void setup() {
        Clock clock = Clock.fixed(NOW.toInstant(), NOW.getZone());

        MongoDatabase db = embeddedMongoRule.getDb();
        worldsCollection = db.getCollection("worlds");
        dao = new MongoWorldDao(clock, worldsCollection);
    }

    /**
     * Test the request to get an unknown document
     */
    @Test
    public void testGetUnknown() {
        Collection<World> worlds = dao.getByIds(Collections.singleton(new WorldId("unknown")));
        Assertions.assertThat(worlds).isEmpty();
    }

    /**
     * Test the request to get an unknown document
     */
    @Test
    public void testGetSingle() {
        WorldId worldId = new WorldId(UUID.randomUUID().toString());

        Document newDocument = new Document();
        newDocument.append("_id", worldId.getId());
        newDocument.append("version", 5L);
        newDocument.append("createdDate",
            Date.from(OffsetDateTime.of(2015, 7, 16, 7, 10, 0, 0, ZoneOffset.UTC).toInstant()));
        newDocument.append("modifiedDate",
            Date.from(OffsetDateTime.of(2015, 7, 16, 7, 10, 25, 0, ZoneOffset.UTC).toInstant()));
        newDocument.append("name", "Test World");
        newDocument.append("description", "This is a test world");
        worldsCollection.insertOne(newDocument);

        Collection<World> worlds = dao.getByIds(Collections.singleton(worldId));
        Assertions.assertThat(worlds).hasSize(1);
        World world = worlds.iterator().next();
        Assertions.assertThat(world).isNotNull();
        Assertions.assertThat(world.getName()).isEqualTo("Test World");
        Assertions.assertThat(world.getDescription()).isEqualTo("This is a test world");
        Assertions.assertThat(world.getId()).isPresent();
        IdDetails<WorldId> worldIdDetails = world.getId().get();
        Assertions.assertThat(worldIdDetails.getId()).isEqualTo(worldId);
        Assertions.assertThat(worldIdDetails.getVersion()).isEqualTo(5L);
        Assertions.assertThat(worldIdDetails.getCreatedDate())
            .isEqualTo(OffsetDateTime.of(2015, 7, 16, 7, 10, 0, 0, ZoneOffset.UTC));
        Assertions.assertThat(worldIdDetails.getLastModifiedDate())
            .isEqualTo(OffsetDateTime.of(2015, 7, 16, 7, 10, 25, 0, ZoneOffset.UTC));
    }

    /**
     * Test the request to save a new document
     */
    @Test
    public void testCreateNew() {
        World world = new World();
        world.setName("Testing World");
        world.setDescription("A new world");

        World saved = dao.save(world);

        Document retrievedDocument =
            worldsCollection.find(new BasicDBObject("_id", saved.getId().get().getId().getId()))
                .limit(1)
                .first();
        Assertions.assertThat(retrievedDocument).isNotNull()
            .containsEntry("_id", saved.getId().get().getId().getId())
            .containsEntry("name", "Testing World")
            .containsEntry("description", "A new world")
            .containsEntry("version", 1L)
            .containsEntry("createdDate", Date.from(NOW.toInstant()))
            .containsEntry("modifiedDate", Date.from(NOW.toInstant()))
            .doesNotContainKey("deletedDate");
    }

    /**
     * Test the request to update an existing document
     */
    @Test
    public void testUpdate() {
        WorldId worldId = new WorldId(UUID.randomUUID().toString());

        Document newDocument = new Document();
        newDocument.append("_id", worldId.getId());
        newDocument.append("version", 5L);
        newDocument.append("createdDate",
            Date.from(OffsetDateTime.of(2015, 7, 14, 7, 10, 0, 0, ZoneOffset.UTC).toInstant()));
        newDocument.append("modifiedDate",
            Date.from(OffsetDateTime.of(2015, 7, 14, 7, 10, 25, 0, ZoneOffset.UTC).toInstant()));
        newDocument.append("name", "Test World");
        newDocument.append("description", "This is a test world");
        worldsCollection.insertOne(newDocument);

        World world = new World(new IdDetails<>(worldId,
            NOW.toOffsetDateTime(),
            NOW.toOffsetDateTime(),
            5L));
        world.setName("Updated World");
        world.setDescription("An old world");

        World saved = dao.save(world);

        Document retrievedDocument =
            worldsCollection.find(new BasicDBObject("_id", worldId.getId()))
                .limit(1)
                .first();
        Assertions.assertThat(retrievedDocument).isNotNull()
            .containsEntry("_id", worldId.getId())
            .containsEntry("name", "Updated World")
            .containsEntry("description", "An old world")
            .containsEntry("version", 6L)
            .containsEntry("createdDate",
                Date.from(OffsetDateTime.of(2015, 7, 14, 7, 10, 0, 0, ZoneOffset.UTC).toInstant()))
            .containsEntry("modifiedDate", Date.from(NOW.toInstant()))
            .doesNotContainKey("deletedDate");
    }

    /**
     * Test deleting a record that isn't known
     */
    @Test
    public void testDeleteUnknown() {
        WorldId worldId = new WorldId("unknown");
        dao.delete(worldId);

        Document retrievedDocument =
            worldsCollection.find(new BasicDBObject("_id", worldId.getId()))
                .limit(1)
                .first();
        Assertions.assertThat(retrievedDocument).isNull();
    }

    /**
     * Test deleting a record that is known
     */
    @Test
    public void testDeleteKnown() {
        WorldId worldId = new WorldId(UUID.randomUUID().toString());

        Document newDocument = new Document();
        newDocument.append("_id", worldId.getId());
        newDocument.append("name", "Test World");
        newDocument.append("description", "This is a test world");
        worldsCollection.insertOne(newDocument);

        Document retrievedDocument =
            worldsCollection.find(new BasicDBObject("_id", worldId.getId()))
            .limit(1)
            .first();
        Assertions.assertThat(retrievedDocument).isNotNull()
            .doesNotContainKey("deletedDate");

        dao.delete(worldId);

        retrievedDocument =
            worldsCollection.find(new BasicDBObject("_id", worldId.getId()))
                .limit(1)
                .first();
        Assertions.assertThat(retrievedDocument).isNotNull()
            .containsEntry("name", "Test World")
            .containsEntry("description", "This is a test world")
            .containsKey("deletedDate");
    }
}
