package uk.co.grahamcox.worldbuilder.webapp.worlds;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.co.grahamcox.worldbuilder.webapp.users.UserLink;

import java.time.OffsetDateTime;

/**
 * JSON Model to represent the details of a world
 */
public class WorldModel extends EditableWorldModel {
    /** The ID of the world */
    @JsonProperty("id")
    private String id;

    /** The creation date of the worldbuilder representation of the world */
    @JsonProperty("creation_date")
    private OffsetDateTime creationDate;

    /** The link to the user that is the owner of this world */
    @JsonProperty("owner")
    private UserLink owner;

    /**
     * Get the ID
     * @return the ID
     */
    public String getId() {
        return id;
    }

    /**
     * Set the ID
     * @param id the ID
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Get when the world was created
     * @return the creation date
     */
    public OffsetDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Set when the world was created
     * @param creationDate the creation date
     */
    public void setCreationDate(final OffsetDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Get the owner
     * @return the owner
     */
    public UserLink getOwner() {
        return owner;
    }

    /**
     * Set the owner
     * @param owner the owner
     */
    public void setOwner(final UserLink owner) {
        this.owner = owner;
    }
}
