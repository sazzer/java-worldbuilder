package uk.co.grahamcox.worldbuilder.model;

import java.util.Objects;

/**
 * Base class for the ID of an entity
 * @param <T> The type of value to use for the ID
 */
public abstract class Id<T> {
    /** The ID of the world */
    private final T id;

    /**
     * Construct the ID
     * @param id the ID
     */
    protected Id(final T id) {
        this.id = id;
    }

    /**
     * Get the ID
     * @return the ID
     */
    public T getId() {
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
        final Id otherId = (Id) o;
        return Objects.equals(id, otherId.id);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
            "id='" + id + '\'' +
            '}';
    }
}
