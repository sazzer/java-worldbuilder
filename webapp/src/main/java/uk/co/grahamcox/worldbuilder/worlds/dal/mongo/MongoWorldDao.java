package uk.co.grahamcox.worldbuilder.worlds.dal.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.grahamcox.worldbuilder.model.IdDetails;
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
        BasicDBObject filter = new BasicDBObject("_id", new BasicDBObject(
            "$in", worldIds.stream().map(WorldId::getId).collect(Collectors.toList())
        ));
        LOG.debug("Looking for Worlds with filter {}", filter);

        FindIterable<Document> documents = collection.find(filter);

        List<World> result = new ArrayList<>();
        documents.map(this::mapDocumentToWorld)
            .into(result);
        return result;
    }

    /**
     * Map a document into a World object
     * @param document the document
     * @return the World object
     */
    private World mapDocumentToWorld(Document document) {
        LOG.debug("Converting document {} into World object", document);

        WorldId worldId = new WorldId(document.getString("_id"));
        OffsetDateTime created =
            OffsetDateTime.parse(document.getString("createdDate"), DateTimeFormatter.ISO_DATE_TIME);
        OffsetDateTime modified =
            OffsetDateTime.parse(document.getString("modifiedDate"), DateTimeFormatter.ISO_DATE_TIME);
        Long version = document.getLong("version");

        World world = new World(new IdDetails<>(worldId, created, modified, version));
        world.setName(document.getString("name"));
        world.setDescription(document.getString("description"));

        return world;
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
        String deletedDate = ZonedDateTime.now(clock).format(DateTimeFormatter.ISO_DATE_TIME);

        BasicDBObject idFilter = new BasicDBObject("_id", worldId.getId());
        LOG.debug("Deleting object at {} based on filter: {}", deletedDate, idFilter);

        collection.updateOne(idFilter, new BasicDBObject("$set",
            new BasicDBObject("deletedDate", deletedDate)));
    }
}
