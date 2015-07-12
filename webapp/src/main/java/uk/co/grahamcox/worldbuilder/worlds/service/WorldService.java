package uk.co.grahamcox.worldbuilder.worlds.service;

import uk.co.grahamcox.worldbuilder.worlds.model.World;
import uk.co.grahamcox.worldbuilder.worlds.model.WorldId;

import java.util.Optional;

/**
 * Service layer for interacting with Worlds
 */
public interface WorldService {
    /**
     * Try and retrieve a single World by it's unique ID
     * @param id the ID of the world
     * @return the world, or {@link Optional#empty()} if there isn't one with this ID
     */
    Optional<World> getWorldById(final WorldId id);

    /**
     * Save the given world, either creating a new one or updating an existing one
     * @param world the world to save
     * @return the newly persisted world
     */
    World saveWorld(final World world);

    /**
     * Delete the given world
     * @param world the world to delete
     */
    void deleteWorld(final World world);
}
