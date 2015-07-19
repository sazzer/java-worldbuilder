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
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * Unit Tests for the MongoWorldDao
 */
public class MongoWorldDaoTest {
    /** The current time */
    private static final OffsetDateTime NOW = OffsetDateTime.of(2015, 7, 15, 13, 11, 0, 0, ZoneOffset.UTC);

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
        Clock clock = Clock.fixed(NOW.toInstant(), NOW.getOffset());

        MongoDatabase db = embeddedMongoRule.getDb();
        worldsCollection = db.getCollection("worlds");
        dao = new MongoWorldDao(clock, worldsCollection);

        worldsCollection.insertMany(Arrays.asList(
            new Document("_id", "00000001-0000-0000-0000-000000000001")
                .append("version", 5L)
                .append("createdDate", Date.from(NOW.toInstant()))
                .append("modifiedDate", Date.from(NOW.toInstant()))
                .append("name", "Test World")
                .append("description", "World for Testing"),
            new Document("_id", "00000001-0000-0000-0000-000000000002")
                .append("version", 1L)
                .append("createdDate", Date.from(NOW.minusDays(5).toInstant()))
                .append("modifiedDate", Date.from(NOW.toInstant()))
                .append("name", "Another Test World")
                .append("description", "Another World for Testing"),
            new Document("_id", "00000001-0000-0000-0000-000000000003")
                .append("version", 1L)
                .append("createdDate", Date.from(NOW.toInstant()))
                .append("modifiedDate", Date.from(NOW.toInstant()))
                .append("name", "Test World to Delete")
                .append("description", "World for Testing Delete")
        ));
    }

    /**
     * Test the request to get an unknown document
     */
    @Test
    public void testGetUnknown() {
        Collection<World> worlds =
            dao.getByIds(Collections.singleton(new WorldId("00000001-0000-0000-0000-000000000000")));
        Assertions.assertThat(worlds).isEmpty();
    }

    /**
     * Test the request to get an unknown document
     */
    @Test
    public void testGetSingle() {
        WorldId worldId = new WorldId("00000001-0000-0000-0000-000000000001");

        Collection<World> worlds = dao.getByIds(Collections.singleton(worldId));
        Assertions.assertThat(worlds).hasSize(1);
        World world = worlds.iterator().next();
        Assertions.assertThat(world).isNotNull();
        Assertions.assertThat(world.getName()).isEqualTo("Test World");
        Assertions.assertThat(world.getDescription()).isEqualTo("World for Testing");
        Assertions.assertThat(world.getId()).isPresent();
        IdDetails<WorldId> worldIdDetails = world.getId().get();
        Assertions.assertThat(worldIdDetails.getId()).isEqualTo(worldId);
        Assertions.assertThat(worldIdDetails.getVersion()).isEqualTo(5L);
        Assertions.assertThat(worldIdDetails.getCreatedDate()).isEqualTo(NOW);
        Assertions.assertThat(worldIdDetails.getLastModifiedDate()).isEqualTo(NOW);
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
        WorldId worldId = new WorldId("00000001-0000-0000-0000-000000000002");

        World world = new World(new IdDetails<>(worldId,
            NOW,
            NOW,
            1L));
        world.setName("Updated World");
        world.setDescription("An old world");

        dao.save(world);

        Document retrievedDocument =
            worldsCollection.find(new BasicDBObject("_id", worldId.getId()))
                .limit(1)
                .first();
        Assertions.assertThat(retrievedDocument).isNotNull()
            .containsEntry("_id", worldId.getId())
            .containsEntry("name", "Updated World")
            .containsEntry("description", "An old world")
            .containsEntry("version", 2L)
            .containsEntry("createdDate", Date.from(NOW.minusDays(5).toInstant()))
            .containsEntry("modifiedDate", Date.from(NOW.toInstant()))
            .doesNotContainKey("deletedDate");
    }

    /**
     * Test deleting a record that isn't known
     */
    @Test
    public void testDeleteUnknown() {
        WorldId worldId = new WorldId("00000001-0000-0000-0000-000000000000");
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
        WorldId worldId = new WorldId("00000001-0000-0000-0000-000000000003");

        Document retrievedDocument =
            worldsCollection.find(new BasicDBObject("_id", worldId.getId()))
            .limit(1)
            .first();
        Assertions.assertThat(retrievedDocument).isNotNull()
            .containsEntry("name", "Test World to Delete")
            .containsEntry("description", "World for Testing Delete")
            .doesNotContainKey("deletedDate");

        dao.delete(worldId);

        retrievedDocument =
            worldsCollection.find(new BasicDBObject("_id", worldId.getId()))
                .limit(1)
                .first();
        Assertions.assertThat(retrievedDocument).isNotNull()
            .containsEntry("name", "Test World to Delete")
            .containsEntry("description", "World for Testing Delete")
            .containsKey("deletedDate");
    }
}
