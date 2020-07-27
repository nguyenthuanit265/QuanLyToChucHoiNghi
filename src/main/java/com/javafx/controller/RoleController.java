package com.javafx.controller;

import com.javafx.entity.Role;
import com.javafx.repository.RoleRepository;
import com.javafx.repository.impl.RoleRepositoryImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.List;

public class RoleController {

    RoleRepository roleRepository = new RoleRepositoryImpl();

    public void processRole(TableView<Object> tableViews) {
        tableViews.getColumns().clear();
        List<Role> roles = roleRepository.findAllActive();
        for (Role role : roles) {
            System.out.println(role.toString());
        }
        final ObservableList<Object> data = FXCollections.observableArrayList(roles);

        TableColumn idCol = new TableColumn("Id");
        idCol.setMinWidth(100);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Role, Integer>("id"));

        TableColumn roleNameCol = new TableColumn("Role Name");
        roleNameCol.setMinWidth(100);
        roleNameCol.setCellValueFactory(
                new PropertyValueFactory<Role, String>("name"));

        TableColumn descriptionCol = new TableColumn("Last Name");
        descriptionCol.setMinWidth(100);
        descriptionCol.setCellValueFactory(
                new PropertyValueFactory<Role, String>("description"));

        tableViews.getColumns().addAll(idCol, roleNameCol, descriptionCol);

        TableColumn actionCol = new TableColumn<Role, Void>("Action");
        actionCol.setMinWidth(100);
//        actionCol.setCellValueFactory(new PropertyValueFactory<Role, Role>("id"));
        actionCol.setCellFactory(param -> new TableCell<Role, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            private final HBox pane = new HBox(deleteButton, editButton);

            {
                editButton.setStyle("-fx-background-color: #74d4c0; -fx-text-fill: white;-fx-cursor: hand;");
                deleteButton.setStyle("-fx-background-color: #d9455f; -fx-text-fill: white;-fx-cursor: hand;");

                editButton.setMaxWidth(Double.MAX_VALUE);
                deleteButton.setMaxWidth(Double.MAX_VALUE);

                HBox.setHgrow(editButton, Priority.ALWAYS);
                HBox.setHgrow(deleteButton, Priority.ALWAYS);
                pane.setSpacing(10);


                deleteButton.setOnAction(event -> {
                    Role getPatient = getTableView().getItems().get(getIndex());
                    System.out.println(getPatient.getId() + "   " + getPatient.getName());
                });

                editButton.setOnAction(event -> {
                    Role getPatient = getTableView().getItems().get(getIndex());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                setGraphic(empty ? null : pane);
            }
        });


        tableViews.getColumns().add(actionCol);
        tableViews.setItems(data);
    }
}
