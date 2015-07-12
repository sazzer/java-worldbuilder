package uk.co.grahamcox.worldbuilder.webapp.worlds;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

/**
 * JSON Model to represent the details of a world
 */
public class WorldModel {
    /** The ID of the world */
    @JsonProperty("id")
    private String id;

    /** The name of the world */
    @JsonProperty("name")
    private String name;

    /** The description of the world */
    @JsonProperty("description")
    private String description;

    /** The creation date of the worldbuilder representation of the world */
    @JsonProperty("creation_date")
    private ZonedDateTime creationDate;

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

    /**
     * Get when the world was created
     * @return the creation date
     */
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Set when the world was created
     * @param creationDate the creation date
     */
    public void setCreationDate(final ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
