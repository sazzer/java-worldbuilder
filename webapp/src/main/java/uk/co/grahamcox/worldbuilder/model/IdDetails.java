package uk.co.grahamcox.worldbuilder.model;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Wrapper around the details that only exist in a persisted object.
 * Specifically, this object contains the ID, Created Date, Last Modified Date and Version of the entity
 * @param <I> The type of ID to use
 */
public final class IdDetails<I extends Id<?>> {
    /** The ID of the entity */
    private final I id;

    /** The created date of the entity */
    private final OffsetDateTime createdDate;

    /** The last modified date of the entity */
    private final OffsetDateTime lastModifiedDate;

    /** the version of the entity */
    private final long version;

    /**
     * Consruct the object
     * @param id the ID
     * @param createdDate the created date
     * @param lastModifiedDate the last modified date
     * @param version the version number
     */
    public IdDetails(final I id,
        final OffsetDateTime createdDate,
        final OffsetDateTime lastModifiedDate,
        final long version) {

        this.id = id;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
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
     * Get the Created Date
     * @return the Created Date
     */
    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     * Get the Last Modified Date
     * @return the Last Modified Date
     */
    public OffsetDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Get the version number
     * @return the version number
     */
    public long getVersion() {
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
        final IdDetails<?> idDetails = (IdDetails<?>) o;
        return Objects.equals(version, idDetails.version) &&
            Objects.equals(id, idDetails.id) &&
            Objects.equals(createdDate, idDetails.createdDate) &&
            Objects.equals(lastModifiedDate, idDetails.lastModifiedDate);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, lastModifiedDate, version);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "IdDetails{" +
            "id=" + id +
            ", createdDate=" + createdDate +
            ", lastModifiedDate=" + lastModifiedDate +
            ", version=" + version +
            '}';
    }
}
