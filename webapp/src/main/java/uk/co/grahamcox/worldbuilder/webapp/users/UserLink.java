package uk.co.grahamcox.worldbuilder.webapp.users;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Subset of user details to use as a link from another object
 */
public class UserLink {
    /** The ID of the user */
    @JsonProperty("id")
    private String id;

    /** The screen name of the user */
    @JsonProperty("name")
    private String name;

    /**
     * Get the ID of the user
     * @return the ID of the user
     */
    public String getId() {
        return id;
    }

    /**
     * Set the ID of the user
     * @param id the ID of the user
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Get the name of the user
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the user
     * @param name the name of the user
     */
    public void setName(final String name) {
        this.name = name;
    }
}
