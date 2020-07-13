package com.javafx.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "locations")
@Data
public class Location implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String address;
    private int capacity;

    @OneToMany(mappedBy = "location",
            fetch = FetchType.LAZY)
    private List<Conference> conferences;

}
