package uk.co.grahamcox.worldbuilder.worlds.model;

import uk.co.grahamcox.worldbuilder.model.Id;

/**
 * Representation of the unique ID of a world
 */
public final class WorldId extends Id<String> {
    /**
     * Construct the ID
     * @param id the raw ID
     */
    public WorldId(final String id) {
        super(id);
    }
}
