package com.javafx.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "conference")
@Data
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location",
            insertable = false, updatable = false)
    private Location location;


    @OneToMany(mappedBy = "conference",
            fetch = FetchType.LAZY)
    private List<Account> accounts;


    @Column(name = "is_delete")
    private boolean isDelete;

    @Override
    public String toString() {
        return "Conference{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", descriptionSummary='" + descriptionSummary + '\'' +
                ", descriptionDetail='" + descriptionDetail + '\'' +
                ", image='" + image + '\'' +
                ", timeStartEvent=" + timeStartEvent +
                ", idLocation=" + idLocation +
                '}';
    }
}
