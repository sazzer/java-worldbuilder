package uk.co.grahamcox.worldbuilder.mongo;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the Embedded Mongo
 */
public class EmbeddedMongoTest {
    /**
     * Test starting the server
     * @throws Exception never
     */
    @Test
    public void testStartServer() throws Exception {
        try (EmbeddedMongo embeddedMongo = new EmbeddedMongo()) {
            embeddedMongo.start();
        }
    }

    /**
     * Test starting the server twice
     * @throws Exception never
     */
    @Test(expected = IllegalStateException.class)
    public void testStartServerTwice() throws Exception {
        try (EmbeddedMongo embeddedMongo = new EmbeddedMongo()) {
            embeddedMongo.start();
            embeddedMongo.start();
        }
    }

    /**
     * Test getting the connection to a server
     * @throws Exception never
     */
    @Test
    public void testGetServerConnection() throws Exception {
        try (EmbeddedMongo embeddedMongo = new EmbeddedMongo()) {
            embeddedMongo.start();
            MongoDatabase db = embeddedMongo.getDb();
            MongoIterable<String> collectionNames = db.listCollectionNames();
            Assert.assertNotNull(collectionNames);
        }
    }

    /**
     * Test getting the connection to a server when the server isn't started
     * @throws Exception never
     */
    @Test(expected = IllegalStateException.class)
    public void testGetServerConnectionNotStarted() throws Exception {
        try (EmbeddedMongo embeddedMongo = new EmbeddedMongo()) {
            embeddedMongo.getDb();
        }
    }
}
