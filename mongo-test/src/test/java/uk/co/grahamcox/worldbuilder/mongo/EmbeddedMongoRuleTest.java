package uk.co.grahamcox.worldbuilder.mongo;

import com.mongodb.client.MongoIterable;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for the Embedded Mongo Rule
 */
public class EmbeddedMongoRuleTest {
    /** The rule to start an embedded MongoDB */
    @Rule
    public EmbeddedMongoRule embeddedMongoRule = new EmbeddedMongoRule();

    /**
     * The test to run
     */
    @Test
    public void test() {
        MongoIterable<String> collectionNames = embeddedMongoRule.getDb().listCollectionNames();
        Assert.assertNotNull(collectionNames);
    }
}
