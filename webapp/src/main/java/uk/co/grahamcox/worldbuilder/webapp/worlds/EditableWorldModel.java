package uk.co.grahamcox.worldbuilder.webapp.worlds;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JSON Model to represent the details of a world that can be edited
 */
public class EditableWorldModel {
    /** The name of the world */
    @JsonProperty("name")
    private String name;

    /** The description of the world */
    @JsonProperty("description")
    private String description;

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
}
