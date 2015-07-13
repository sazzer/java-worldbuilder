package uk.co.grahamcox.worldbuilder.worlds.dal;

import uk.co.grahamcox.worldbuilder.worlds.model.World;
import uk.co.grahamcox.worldbuilder.worlds.model.WorldId;

import java.util.Collection;

/**
 * Interface describing the Worlds Data Store
 */
public interface WorldDao {
    /**
     * Get a list of Worlds with the given IDs
     * @param worldIds the IDs
     * @return the Worlds
     */
    Collection<World> getByIds(final Collection<WorldId> worldIds);

    /**
     * Save the given World to the data store
     * @param world the world
     * @return the newly persisted world
     */
    World save(final World world);

    /**
     * Delete the world with the given ID
     * @param worldId the world to delete
     */
    void delete(final WorldId worldId);
}
