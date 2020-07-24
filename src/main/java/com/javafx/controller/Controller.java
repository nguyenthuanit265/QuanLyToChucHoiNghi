//package com.javafx.controller;
//
//import com.javafx.entity.Role;
//import com.javafx.repository.RoleRepository;
//import com.javafx.repository.TestRepository;
//import com.javafx.repository.impl.RoleRepositoryImpl;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.List;
//
//@Component
//public class Controller {
//    private ObservableList<Role> listBooks;
//    private ConfigurableApplicationContext springContext;
////    RoleRepository roleRepository = new RoleRepositoryImpl();
//
////    @Autowired
////    TestRepository testRepository;
//
//
//    @Autowired
//    RoleRepository roleRepository;
//
//
//    @PostConstruct
//    public void init() {
//        Role role = new Role("ROLE_ADMIN", "admin");
//        roleRepository.save(role);
//    }
//
//    public void populateTableViewBooks() {
//        //txtfldSearchBook.setText(Double.toString(btnAddBook.getHeight()));
//        if (listBooks != null && listBooks.size() != 0) {
//            listBooks.removeAll();
//        }
//
//        List<Role> books = roleRepository.findAll();
//        listBooks = FXCollections.observableList(books);
//
//        for (Role role : books) {
//            System.out.println(role.toString());
//        }
//    }
//
//}
