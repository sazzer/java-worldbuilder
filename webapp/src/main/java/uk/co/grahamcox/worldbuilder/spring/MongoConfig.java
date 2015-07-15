package uk.co.grahamcox.worldbuilder.spring;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.web.util.UriComponentsBuilder;
import uk.co.grahamcox.worldbuilder.mongo.EmbeddedMongo;

import java.net.URI;
import java.util.Optional;

/**
 * Spring configuration to connect to a MongoDB store
 */
@Configuration
public class MongoConfig {
    /** The logger to use */
    private static final Logger LOG = LoggerFactory.getLogger(MongoConfig.class);

    /** The Spring Environment */
    @Autowired
    private Environment environment;

    /**
     * Build an Embedded MongoDB store, if needed
     * @return the Embedded MongoDB
     */
    @Bean(initMethod = "start", destroyMethod = "close")
    @Lazy
    public EmbeddedMongo buildEmbeddedMongo() {
        return new EmbeddedMongo();
    }

    /**
     * Connect to the MongoDB Store
     * @return the DB connection
     * @throws Exception if the connection to the MongoDB store fails
     */
    @Bean
    public MongoDatabase getMongoDb() {
        return Optional.ofNullable(environment.getProperty("uri.mongodb"))
            .map(uri -> UriComponentsBuilder.fromUriString(uri).build().toUri())
            .map(this::getRemoteMongoDb)
            .orElseGet(this::getEmbeddedMongoDb);
    }

    /**
     * Get the connection to a remote MongoDB database
     * @param uri the URI to the database
     * @return the connection to the database
     * @throws Exception if anything goes wrong connecting to the database
     */
    private MongoDatabase getRemoteMongoDb(final URI uri) {
        LOG.info("Creating a MongoDB connection to {}", uri);
        String host = uri.getHost();
        int port = uri.getPort();
        String dbName = uri.getPath();

        MongoClient mongoClient = new MongoClient(host, port);
        MongoDatabase database = mongoClient.getDatabase(dbName);
        return database;
    }

    /**
     * Get the MongoDB connection to an Embedded database
     * @return the connection
     * @throws Exception if creating the embedded database fails
     */
    private MongoDatabase getEmbeddedMongoDb() {
        LOG.info("Starting an Embedded MongoDB connection");
        EmbeddedMongo embeddedMongo = buildEmbeddedMongo();
        return embeddedMongo.getDb();
    }
}
