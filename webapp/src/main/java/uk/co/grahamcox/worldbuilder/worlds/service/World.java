package uk.co.grahamcox.worldbuilder.worlds.service;

import uk.co.grahamcox.worldbuilder.model.IdDetails;

import java.util.Optional;

/**
 * Representation of a World
 */
public class World {
    /** The ID details */
    private final Optional<IdDetails<WorldId>> id;

    /** The name of the world */
    private String name;

    /** The description of the world */
    private String description;

    /**
     * Construct a World with an ID
     * @param id the ID of the world
     */
    public World(final IdDetails<WorldId> id) {
        this.id = Optional.of(id);
    }

    /**
     * Construct a World without an ID
     */
    public World() {
        this.id = Optional.empty();
    }

    /**
     * Get the ID
     * @return the ID
     */
    public Optional<IdDetails<WorldId>> getId() {
        return id;
    }

    /**
     * Get the name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name
     * @param name the name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Get the description
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description
     * @param description the description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "World{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            '}';
    }
}
