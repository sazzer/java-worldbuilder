package uk.co.grahamcox.worldbuilder.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Optional;

/**
 * Wrapper around an embedded MongoDB data store
 */
public class EmbeddedMongo implements AutoCloseable {
    /** The logger to use */
    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedMongo.class);

    /** The mongo executable */
    private Optional<MongodExecutable> mongodExecutable = Optional.empty();

    /** The port to listen on */
    private int port;

    /**
     * Start the embedded MongoDB data store
     * @throws Exception if anything goes wrong
     */
    public void start() throws Exception {
        if (mongodExecutable.isPresent()) {
            throw new IllegalStateException("Server is already running");
        }

        port = Optional.ofNullable(System.getProperty("port.mongodb"))
            .map(Integer::parseInt)
            .orElseGet(() -> {
                try {
                    return new ServerSocket(0).getLocalPort();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        LOG.debug("Starting MongoDB server on port {}", port);
        IMongodConfig mongodConfig = new MongodConfigBuilder()
            .version(Version.Main.PRODUCTION)
            .net(new Net(port, Network.localhostIsIPv6()))
            .build();

        MongodStarter mongodStarter = MongodStarter.getDefaultInstance();
        mongodExecutable = Optional.of(mongodStarter.prepare(mongodConfig));

        mongodExecutable.get().start();
        LOG.debug("Started MongoDB server on port {}", port);
    }

    /**
     * Get the Mongo Database connection to use
     * @return the Mongo Database connection
     */
    public MongoDatabase getDb() {
        if (mongodExecutable.isPresent()) {
            MongoClient mongoClient = new MongoClient("localhost", port);
            return mongoClient.getDatabase("test");
        } else {
            throw new IllegalStateException("Server is not running");
        }
    }
    /**
     * Close the embedded MongoDB data store
     * @throws Exception if anything goes wrong
     */
    @Override
    public void close() throws Exception {
        mongodExecutable.ifPresent(e -> {
            LOG.debug("Stopping MongoDB server on port {}", port);
            e.stop();
            mongodExecutable = Optional.empty();
            LOG.debug("Stopped MongoDB server on port {}", port);
        });
    }
}
