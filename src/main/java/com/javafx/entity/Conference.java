package com.javafx.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "conference")
public class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(name = "description_summary")
    private String descriptionSummary;

    @Column(name = "description_detail")
    private String descriptionDetail;
    private String image;

    @Column(name = "time_start")
    private Date timeStartEvent;

    @Column(name = "id_location")
    private int idLocation;

    @ManyToOne()
    @JoinColumn(name = "id_location",
            insertable = false, updatable = false)
    private Location location;
}
