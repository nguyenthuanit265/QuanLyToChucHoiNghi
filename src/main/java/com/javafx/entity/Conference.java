package com.javafx.entity;

import lombok.Data;

import javax.persistence.*;

import java.util.Date;
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

    @Basic
    @Column(name = "time_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStartEvent;

    @Basic
    @Column(name = "time_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeEndEvent;


    @Column(name = "id_location")
    private int idLocation;

    @ManyToOne()
    @JoinColumn(name = "id_location",
            insertable = false, updatable = false)
    private Location location;


//    @OneToMany(mappedBy = "conference",
//            fetch = FetchType.EAGER)
//    private List<Account> accounts;

    @OneToMany(mappedBy = "conference", fetch = FetchType.EAGER)
    List<Accounts_Conferences> registrations;

    @Column(name = "is_delete")
    private boolean isDelete = false;

    @Column(name = "is_block")
    private boolean isBlock = false;

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
