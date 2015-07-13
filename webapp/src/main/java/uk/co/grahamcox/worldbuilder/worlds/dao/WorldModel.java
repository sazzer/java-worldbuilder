package uk.co.grahamcox.worldbuilder.worlds.dao;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * JPA Model representing a World
 */
@Entity
@Table(name = "worlds")
public class WorldModel {
    /** The ID of the world */
    @Id
    @Column(name = "world_id")
    private String id;

    /** The version of the record */
    @Version
    @Column(name = "version")
    private Long version;

    /** When the record was created */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    private OffsetDateTime created;

    /** When the record was last modified */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified")
    private OffsetDateTime modified;

    /** The name of the world */
    @Column(name = "name")
    private String name;

    /** The description of the world */
    @Column(name = "description")
    private String description;

    /** The ID of the owner of the world */
    @Column(name = "owner_id")
    private String ownerId;
}
