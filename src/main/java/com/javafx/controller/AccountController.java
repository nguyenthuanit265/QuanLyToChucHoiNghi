package com.javafx.controller;

import com.javafx.entity.Account;
import com.javafx.entity.Role;
import com.javafx.repository.AccountRepository;
import com.javafx.repository.impl.AccountRepositoryImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.List;

public class AccountController {
    AccountRepository accountRepository = new AccountRepositoryImpl();

    public void findAll(TableView<Object> tableViews) {
        tableViews.getColumns().clear();
        List<Account> accounts = accountRepository.findAll();
        for (Account role : accounts) {
            System.out.println(role.toString());
        }
        final ObservableList<Object> data = FXCollections.observableArrayList(accounts);

        TableColumn idCol = new TableColumn("Id");
        idCol.setMinWidth(100);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Account, Integer>("id"));

        TableColumn usernameCol = new TableColumn("Username");
        usernameCol.setMinWidth(100);
        usernameCol.setCellValueFactory(
                new PropertyValueFactory<Account, String>("username"));

        TableColumn emailCol = new TableColumn("Email");
        emailCol.setMinWidth(100);
        emailCol.setCellValueFactory(
                new PropertyValueFactory<Account, String>("email"));

        TableColumn roleCol = new TableColumn("Role");
        roleCol.setMinWidth(100);
        roleCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Account, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Account, String> c) {
                return new SimpleStringProperty(c.getValue().getRole().getName());
            }
        });


        tableViews.getColumns().addAll(idCol, usernameCol, emailCol, roleCol);

        TableColumn actionCol = new TableColumn<Role, Void>("Action");
        actionCol.setMinWidth(100);
//        actionCol.setCellValueFactory(new PropertyValueFactory<Role, Role>("id"));
        actionCol.setCellFactory(param -> new TableCell<Account, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            private final HBox pane = new HBox(deleteButton, editButton);

            {
                editButton.setStyle("-fx-background-color: #74d4c0; -fx-text-fill: white");


                deleteButton.setStyle("-fx-background-color: #d9455f; -fx-text-fill: white");


                deleteButton.setOnAction(event -> {
                    Account getPatient = getTableView().getItems().get(getIndex());
                    System.out.println(getPatient.getId() + "   " + getPatient.getEmail());
                });

                editButton.setOnAction(event -> {
                    Account getPatient = getTableView().getItems().get(getIndex());
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
