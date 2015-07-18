package uk.co.grahamcox.worldbuilder.worlds.dal.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import java.util.UUID;

import com.mongodb.client.model.UpdateOptions;
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

    /** Record Field for the ID */
    private static final String ID_FIELD = "_id";

    /** Record Field for the created date */
    private static final String CREATED_DATE_FIELD = "createdDate";

    /** Record Field for the modified date */
    private static final String MODIFIED_DATE_FIELD = "modifiedDate";

    /** Record Field for the version */
    private static final String VERSION_FIELD = "version";

    /** Record Field for the name */
    private static final String NAME_FIELD = "name";

    /** Record Field for the description */
    private static final String DESCRIPTION_FIELD = "description";

    /** Record Field for the deleted date */
    private static final String DELETED_DATE_FIELD = "deletedDate";

    /** The UTC Timezone */
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");

    /** MongoDB operator to set the value of fields */
    private static final String MONGO_SET_FIELDS = "$set";

    /** MongoDB operator to increment the value of fields */
    private static final String MONGO_INCREMENT_FIELDS = "$inc";

    /** MongoDB operator to unset the value of fields */
    private static final String MONGO_UNSET_FIELDS = "$unset";

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
        ZonedDateTime modifiedDate = ZonedDateTime.now(clock).withZoneSameInstant(UTC_ZONE);

        BasicDBObject filter;
        WorldId worldId;
        if (world.getId().isPresent()) {
            filter = buildIdFilter(world.getId().get());
            worldId = world.getId().get().getId();
        } else {
            worldId = new WorldId(UUID.randomUUID().toString());
            filter = buildIdFilter(worldId);
        }

        BasicDBObject updates = new BasicDBObject()
            .append(MONGO_SET_FIELDS, new BasicDBObject()
                .append(NAME_FIELD, world.getName())
                .append(DESCRIPTION_FIELD, world.getDescription())
                .append(MODIFIED_DATE_FIELD, Date.from(modifiedDate.toInstant())))
            .append("$setOnInsert", new BasicDBObject()
                .append(CREATED_DATE_FIELD, Date.from(modifiedDate.toInstant())))
            .append("$inc", new BasicDBObject()
                .append("version", 1L));

        UpdateOptions updateOptions = new UpdateOptions()
            .upsert(true);

        collection.updateOne(filter, updates, updateOptions);

        FindIterable<Document> documents = collection.find(buildIdFilter(worldId));
        return mapDocumentToWorld(documents.first());
    }

    /**
     * Delete the world with the given ID
     *
     * @param worldId the world to delete
     */
    @Override
    public void delete(final WorldId worldId) {
        ZonedDateTime deletedDate = ZonedDateTime.now(clock).withZoneSameInstant(UTC_ZONE);

        BasicDBObject idFilter = buildIdFilter(worldId);
        LOG.debug("Deleting object at {} based on filter: {}", deletedDate, idFilter);

        collection.updateOne(idFilter, new BasicDBObject(MONGO_SET_FIELDS,
            new BasicDBObject(DELETED_DATE_FIELD, Date.from(deletedDate.toInstant()))));
    }

    /**
     * Build the MongoDB Filter for a versioned World ID
     * @param worldIdDetails the ID of the World
     * @return the Filter
     */
    private BasicDBObject buildIdFilter(final IdDetails<WorldId> worldIdDetails) {
        BasicDBObject idFilter = buildIdFilter(worldIdDetails.getId());
        idFilter.append(VERSION_FIELD, worldIdDetails.getVersion());
        return idFilter;
    }

    /**
     * Build the MongoDB Filter for a World ID
     * @param worldId the ID of the World
     * @return the Filter
     */
    private BasicDBObject buildIdFilter(final WorldId worldId) {
        BasicDBObject idFilter = new BasicDBObject();
        idFilter.append(ID_FIELD, worldId.getId());
        return idFilter;
    }
}
