package uk.co.grahamcox.worldbuilder.worlds.dal.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.grahamcox.worldbuilder.model.IdDetails;
import uk.co.grahamcox.worldbuilder.worlds.dal.WorldDao;
import uk.co.grahamcox.worldbuilder.worlds.model.World;
import uk.co.grahamcox.worldbuilder.worlds.model.WorldId;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Worlds DAO that works in terms of a MongoDB store
 */
public class MongoWorldDao implements WorldDao {
    /** The logger to use */
    private static final Logger LOG = LoggerFactory.getLogger(MongoWorldDao.class);

    /** Record fild for the ID */
    private static final String ID_FIELD = "_id";

    /** Record fild for the created date */
    private static final String CREATED_DATE_FIELD = "createdDate";

    /** Record fild for the modified date */
    private static final String MODIFIED_DATE_FIELD = "modifiedDate";

    /** Record fild for the version */
    private static final String VERSION_FIELD = "version";

    /** Record fild for the name */
    private static final String NAME_FIELD = "name";

    /** Record fild for the description */
    private static final String DESCRIPTION_FIELD = "description";

    /** The UTC Timezone */
    public static final ZoneId UTC_ZONE = ZoneId.of("UTC");

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
        BasicDBObject filter = new BasicDBObject(ID_FIELD, new BasicDBObject(
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
    private World mapDocumentToWorld(final Document document) {
        LOG.debug("Converting document {} into World object", document);

        WorldId worldId = new WorldId(document.getString(ID_FIELD));
        OffsetDateTime created =
            OffsetDateTime.ofInstant(document.getDate(CREATED_DATE_FIELD).toInstant(), UTC_ZONE);
        OffsetDateTime modified =
            OffsetDateTime.ofInstant(document.getDate(MODIFIED_DATE_FIELD).toInstant(), UTC_ZONE);
        Long version = document.getLong(VERSION_FIELD);

        World world = new World(new IdDetails<>(worldId, created, modified, version));
        world.setName(document.getString(NAME_FIELD));
        world.setDescription(document.getString(DESCRIPTION_FIELD));

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
        ZonedDateTime deletedDate = ZonedDateTime.now(clock).withZoneSameInstant(UTC_ZONE);

        BasicDBObject idFilter = new BasicDBObject(ID_FIELD, worldId.getId());
        LOG.debug("Deleting object at {} based on filter: {}", deletedDate, idFilter);

        collection.updateOne(idFilter, new BasicDBObject("$set",
            new BasicDBObject("deletedDate", Date.from(deletedDate.toInstant()))));
    }
}
