package com.javafx.controller;

import com.javafx.entity.Account;
import com.javafx.entity.Role;
import com.javafx.repository.AccountRepository;
import com.javafx.repository.impl.AccountRepositoryImpl;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class AccountController {
    AccountRepository accountRepository = new AccountRepositoryImpl();

    public AccountController() {
        String hashed = BCrypt.hashpw("123456", BCrypt.gensalt());
//        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
//        accountRepository.save(new Account("test@gmai.com", "test", hashed, 1, 1));
    }

    public void ProcessAccount(TableView<Object> tableViews) {


        tableViews.getColumns().clear();
        List<Account> accounts = accountRepository.findAllActive();
//        tableViews.setFixedCellSize(25);
//        tableViews.prefHeightProperty().bind(Bindings.size(tableViews.getItems()).multiply(tableViews.getFixedCellSize()).add(30));

        for (Account role : accounts) {
            System.out.println(role.toString());
        }
        final ObservableList<Object> data = FXCollections.observableArrayList(accounts);

        TableColumn numberCol = new TableColumn("#");
        numberCol.setMinWidth(20);
        numberCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Account, Account>, ObservableValue<Account>>() {
            @Override
            public ObservableValue<Account> call(TableColumn.CellDataFeatures<Account, Account> p) {
                return new ReadOnlyObjectWrapper(p.getValue());
            }
        });

        numberCol.setCellFactory(new Callback<TableColumn<Account, Account>, TableCell<Account, Account>>() {
            @Override
            public TableCell<Account, Account> call(TableColumn<Account, Account> param) {
                return new TableCell<Account, Account>() {
                    @Override
                    protected void updateItem(Account item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && item != null) {
                            setText(this.getTableRow().getIndex() + 1 + "");
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });
        numberCol.setSortable(false);


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


        tableViews.getColumns().addAll(numberCol, idCol, usernameCol, emailCol, roleCol);

        TableColumn actionCol = new TableColumn<Role, Void>("Action");
        actionCol.setMinWidth(130);
//        actionCol.setCellValueFactory(new PropertyValueFactory<Role, Role>("id"));
        actionCol.setCellFactory(param -> new TableCell<Account, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final Button blockButton = new Button("Block");

            private final HBox pane = new HBox(editButton, deleteButton, blockButton);

            {
                editButton.setStyle("-fx-background-color: #74d4c0; -fx-text-fill: white; -fx-cursor: hand;");
                deleteButton.setStyle("-fx-background-color: #d9455f; -fx-text-fill: white; -fx-cursor: hand;");
                blockButton.setStyle("-fx-background-color: #9a1f40; -fx-text-fill: white; -fx-cursor: hand;");

                editButton.setMaxWidth(Double.MAX_VALUE);
                deleteButton.setMaxWidth(Double.MAX_VALUE);
                blockButton.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(editButton, Priority.ALWAYS);
                HBox.setHgrow(deleteButton, Priority.ALWAYS);
                HBox.setHgrow(blockButton, Priority.ALWAYS);
                pane.setSpacing(10);

                deleteButton.setOnAction(event -> {
                    Account getPatient = getTableView().getItems().get(getIndex());
                    System.out.println(getPatient.getId() + "   " + getPatient.getEmail());
                    getPatient.setDelete(true);
                    accountRepository.save(getPatient);
                    ProcessAccount(tableViews);
                });

                editButton.setOnAction(event -> {
                    Account getPatient = getTableView().getItems().get(getIndex());
                    System.out.println(getPatient.getId() + "   " + getPatient.getEmail());
                    Account account = accountRepository.findById(getPatient.getId());

                    if (BCrypt.checkpw("123456", account.getPassword()))
                        System.out.println("It matches");
                    else
                        System.out.println("It does not match");


                });

                blockButton.setOnAction(event -> {
                    Account getPatient = getTableView().getItems().get(getIndex());
                    System.out.println(getPatient.getId() + "   " + getPatient.getEmail());
                    getPatient.setBlock(true);
                    accountRepository.save(getPatient);
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
