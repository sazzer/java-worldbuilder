package uk.co.grahamcox.worldbuilder.mongo;

import com.mongodb.client.MongoDatabase;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * JUnit rule for starting and stopping an embedded MongoDB server
 */
public class EmbeddedMongoRule implements TestRule {
    /** The database connection */
    private MongoDatabase db;

    /**
     * Get the database connection
     * @return the database connection
     */
    public MongoDatabase getDb() {
        return db;
    }

    /**
     * Apply the rule, starting the server before the test starts and stopping it afterwards
     * @param base the base statement
     * @param description the description
     * @return the new statement to execute
     */
    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try (EmbeddedMongo embeddedMongo = new EmbeddedMongo()) {
                    embeddedMongo.start();
                    db = embeddedMongo.getDb();
                    base.evaluate();
                }
            }
        };
    }
}
