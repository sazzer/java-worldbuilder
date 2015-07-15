package uk.co.grahamcox.worldbuilder.worlds.dal.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.grahamcox.worldbuilder.worlds.dal.WorldDao;
import uk.co.grahamcox.worldbuilder.worlds.model.World;
import uk.co.grahamcox.worldbuilder.worlds.model.WorldId;

/**
 * Worlds DAO that works in terms of a MongoDB store
 */
public class MongoWorldDao implements WorldDao {
    /** The logger to use */
    private static final Logger LOG = LoggerFactory.getLogger(MongoWorldDao.class);

    /** The clock to use */
    private final Clock clock;

    /** The collection that the worlds are stored in */
    private final MongoCollection<Document> collection;

    /**
     * Construct the DAO
     * @param clock The clock to use
     * @param collection the collection the worlds are stored in
     */
    public MongoWorldDao(final Clock clock,
        final MongoCollection<Document> collection) {

        this.clock = clock;
        this.collection = collection;
    }

    /**
     * Get a list of Worlds with the given IDs
     *
     * @param worldIds the IDs
     * @return the Worlds
     */
    @Override
    public Collection<World> getByIds(final Collection<WorldId> worldIds) {
        return null;
    }

    /**
     * Save the given World to the data store
     *
     * @param world the world
     * @return the newly persisted world
     */
    @Override
    public World save(final World world) {
        return null;
    }

    /**
     * Delete the world with the given ID
     *
     * @param worldId the world to delete
     */
    @Override
    public void delete(final WorldId worldId) {
        BasicDBObject idFilter = new BasicDBObject("_id", worldId.getId());
        String deletedDate = ZonedDateTime.now(clock).format(DateTimeFormatter.ISO_DATE_TIME);
        LOG.debug("Deleting object at {} based on filter: {}", deletedDate, idFilter);
        collection.updateOne(idFilter, new BasicDBObject("$set",
            new BasicDBObject("deletedDate", deletedDate)));
    }
}
