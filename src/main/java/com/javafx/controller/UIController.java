package com.javafx.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

//import com.javafx.Launch;
import com.javafx.Main;
import com.javafx.entity.Conference;
import com.javafx.entity.Location;
import com.javafx.entity.Role;
import com.javafx.repository.ConferenceRepository;
import com.javafx.repository.LocationRepository;
import com.javafx.repository.RoleRepository;
import com.javafx.repository.impl.ConferenceRepositoryImpl;
import com.javafx.repository.impl.LocationRepositoryImpl;
import com.javafx.repository.impl.RoleRepositoryImpl;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UIController {


    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private AnchorPane parent;


    RoleRepository roleRepository = new RoleRepositoryImpl();

    ConferenceRepository conferenceRepository = new ConferenceRepositoryImpl();

    LocationRepository locationRepository = new LocationRepositoryImpl();

//    @PostConstruct
//    public void init() {
//        Role role = new Role("ROLE_ADMIN", "admin");
//        roleRepository.save(role);
//    }

//    @Autowired
//    public UIController(RoleRepository roleRepository) {
//        this.roleRepository = roleRepository;
//    }

//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        // TODO
//        makeStageDrageable();
//    }

    private void makeStageDrageable() {
        parent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Main.stage.setX(event.getScreenX() - xOffset);
                Main.stage.setY(event.getScreenY() - yOffset);
                Main.stage.setOpacity(0.7f);
            }
        });
        parent.setOnDragDone((e) -> {
            Main.stage.setOpacity(1.0f);
        });
        parent.setOnMouseReleased((e) -> {
            Main.stage.setOpacity(1.0f);
        });

    }

    public void findAllRole(ActionEvent actionEvent) {

        List<Role> roles = roleRepository.findAll();
        for (Role role : roles) {
            System.out.println(role.toString());
        }

    }

    public void findAllConference(ActionEvent actionEvent) {
        List<Conference> cons = conferenceRepository.findAll();
        for (Conference conference : cons) {
            System.out.println(conference.toString());
        }
    }

    public void findAllLocation(ActionEvent actionEvent) {
        List<Location> locations = locationRepository.findAll();
        for (Location location : locations) {
            System.out.println(location.toString());
        }
    }
}
