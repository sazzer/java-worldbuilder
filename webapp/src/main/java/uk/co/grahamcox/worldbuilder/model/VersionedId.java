package uk.co.grahamcox.worldbuilder.model;

import java.util.Objects;
import java.util.Optional;

/**
 * Wrapper around the ID of an object and optionally the current Version of the data of that object
 * @param <I> The type of ID to use
 */
public final class VersionedId<I extends Id<?>> {
    /** The ID of the entity */
    private final I id;

    /** the version of the entity */
    private final Optional<Long> version;

    /**
     * Consruct the object
     * @param id the ID
     * @param version the version number
     */
    public VersionedId(final I id,
        final Optional<Long> version) {

        this.id = id;
        this.version = version;
    }

    /**
     * Get the ID
     * @return the ID
     */
    public I getId() {
        return id;
    }

    /**
     * Get the version number
     * @return the version number
     */
    public Optional<Long> getVersion() {
        return version;
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
        final VersionedId<?> idDetails = (VersionedId<?>) o;
        return Objects.equals(version, idDetails.version) &&
            Objects.equals(id, idDetails.id);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(id, version);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "VersionedId{" +
            "id=" + id +
            ", version=" + version +
            '}';
    }
}
