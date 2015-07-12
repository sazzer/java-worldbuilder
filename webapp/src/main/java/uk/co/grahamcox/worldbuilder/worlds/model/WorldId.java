package uk.co.grahamcox.worldbuilder.worlds.model;

import java.util.Objects;

/**
 * Representation of the unique ID of a world
 */
public final class WorldId {
    /** The ID of the world */
    private final String id;

    /**
     * Construct the ID
     * @param id the ID
     */
    public WorldId(final String id) {
        this.id = id;
    }

    /**
     * Get the ID
     * @return the ID
     */
    public String getId() {
        return id;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final WorldId worldId = (WorldId) o;
        return Objects.equals(id, worldId.id);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "WorldId{" +
            "id='" + id + '\'' +
            '}';
    }
}
