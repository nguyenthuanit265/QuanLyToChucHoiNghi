package com.javafx.controller;

import com.javafx.entity.Role;
import com.javafx.repository.RoleRepository;
import com.javafx.repository.impl.RoleRepositoryImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

public class Controller {
    private ObservableList<Role> listBooks;

    RoleRepository roleRepository = new RoleRepositoryImpl();

    public Controller() {
        Role role = new Role("ROLE_ADMIN", "admin");
        roleRepository.save(role);
    }

//    @PostConstruct
//    public void init() {
//        Role role = new Role("ROLE_ADMIN", "admin");
//        roleRepository.save(role);
//    }

    public void populateTableViewBooks() {
        //txtfldSearchBook.setText(Double.toString(btnAddBook.getHeight()));
        if (listBooks != null && listBooks.size() != 0) {
            listBooks.removeAll();
        }

        List<Role> books = roleRepository.findAll();
        listBooks = FXCollections.observableList(books);

        for (Role role : books) {
            System.out.println(role.toString());
        }
    }

}
