package uk.co.grahamcox.worldbuilder.spring;

import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.grahamcox.worldbuilder.mongo.EmbeddedMongo;

/**
 * Spring configuration to connect to a MongoDB store
 */
@Configuration
public class MongoConfig {
    /**
     * Build an Embedded MongoDB store, if needed
     * @return the Embedded MongoDB
     * @throws Exception if the MongoDB store fails to start
     */
    @Bean(initMethod = "start", destroyMethod = "close")
    public EmbeddedMongo buildEmbeddedMongo() throws Exception {
        return new EmbeddedMongo();
    }

    /**
     * Connect to the MongoDB Store
     * @return the DB connection
     * @throws Exception if the connection to the MongoDB store fails
     */
    @Bean
    public MongoDatabase getMongoDb() throws Exception {
        EmbeddedMongo embeddedMongo = buildEmbeddedMongo();
        return embeddedMongo.getDb();
    }
}
