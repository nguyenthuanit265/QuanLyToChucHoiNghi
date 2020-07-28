package com.javafx.entity;

import lombok.Data;

import javax.persistence.*;

@Table
@Entity(name = "accounts_conferences")
@Data
public class Accounts_Conferences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;

    @ManyToOne
    @JoinColumn(name = "conference_id")
    Conference conference;



}
