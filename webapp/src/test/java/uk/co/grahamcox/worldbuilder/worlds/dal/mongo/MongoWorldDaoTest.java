package uk.co.grahamcox.worldbuilder.worlds.dal.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import uk.co.grahamcox.worldbuilder.mongo.EmbeddedMongoRule;
import uk.co.grahamcox.worldbuilder.worlds.model.WorldId;

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
        Assert.assertNull(retrievedDocument);
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
        Assert.assertNotNull(retrievedDocument);
        Assert.assertNull(retrievedDocument.get("deletedDate"));

        dao.delete(worldId);

        retrievedDocument =
            worldsCollection.find(new BasicDBObject("_id", worldId.getId()))
                .limit(1)
                .first();
        Assert.assertNotNull(retrievedDocument);
        Assert.assertNotNull(retrievedDocument.get("deletedDate"));
    }
}