package com.javafx.controller;

import com.javafx.entity.Account;
import com.javafx.entity.Accounts_Conferences;
import com.javafx.entity.Conference;
import com.javafx.entity.Role;
import com.javafx.repository.ConferenceRepository;
import com.javafx.repository.impl.ConferenceRepositoryImpl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Date;
import java.util.List;

public class ConferenceController {

    private ConferenceRepository conferenceRepository = new ConferenceRepositoryImpl();

    public void processConference(TableView<Object> tableViews) {
        tableViews.getColumns().clear();
        tableViews.getItems().clear();


        List<Conference> conferences = conferenceRepository.findAllActive();
//        tableViews.setFixedCellSize(25);
//        tableViews.prefHeightProperty().bind(Bindings.size(tableViews.getItems()).multiply(tableViews.getFixedCellSize()).add(30));

        for (Conference conference : conferences) {
            System.out.println(conference.toString());
        }

        final ObservableList<Object> data = FXCollections.observableArrayList(conferences);

        TableColumn numberCol = new TableColumn("#");
        numberCol.setMinWidth(10);
        numberCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Conference, Conference>, ObservableValue<Conference>>() {
            @Override
            public ObservableValue<Conference> call(TableColumn.CellDataFeatures<Conference, Conference> p) {
                return new ReadOnlyObjectWrapper(p.getValue());
            }
        });

        numberCol.setCellFactory(new Callback<TableColumn<Conference, Conference>, TableCell<Conference, Conference>>() {
            @Override
            public TableCell<Conference, Conference> call(TableColumn<Conference, Conference> param) {
                return new TableCell<Conference, Conference>() {
                    @Override
                    protected void updateItem(Conference item, boolean empty) {
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
        idCol.setMinWidth(50);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Conference, Integer>("id"));

        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(50);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Conference, String>("name"));

        TableColumn summaryCol = new TableColumn("Summary");
        summaryCol.setMinWidth(50);
        summaryCol.setCellValueFactory(
                new PropertyValueFactory<Conference, String>("descriptionSummary"));

        TableColumn timeStartCol = new TableColumn("Time Start");
        timeStartCol.setMinWidth(100);
        timeStartCol.setCellValueFactory(
                new PropertyValueFactory<Conference, Date>("timeStartEvent"));

        TableColumn timeEndCol = new TableColumn("Time End");
        timeEndCol.setMinWidth(100);
        timeEndCol.setCellValueFactory(
                new PropertyValueFactory<Conference, Date>("timeEndEvent"));


        TableColumn locationCol = new TableColumn("Location");
        locationCol.setMinWidth(100);
        locationCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Conference, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Conference, String> c) {
                return new SimpleStringProperty(c.getValue().getLocation().getAddress());
            }
        });


        tableViews.getColumns().addAll(numberCol, nameCol, summaryCol, timeStartCol, timeEndCol, locationCol);

        TableColumn actionCol = new TableColumn<Role, Void>("Action");
        actionCol.setMinWidth(150);
//        actionCol.setCellValueFactory(new PropertyValueFactory<Role, Role>("id"));
        actionCol.setCellFactory(param -> new TableCell<Conference, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            private final Button listUserButton = new Button("List User");

            private final HBox pane = new HBox(editButton, deleteButton, listUserButton);

            {
                editButton.setStyle("-fx-background-color: #74d4c0; -fx-text-fill: white; -fx-cursor: hand;");
                deleteButton.setStyle("-fx-background-color: #d9455f; -fx-text-fill: white; -fx-cursor: hand;");
                listUserButton.setStyle("-fx-background-color: #d9455f; -fx-text-fill: white; -fx-cursor: hand;");

                editButton.setMaxWidth(Double.MAX_VALUE);
                deleteButton.setMaxWidth(Double.MAX_VALUE);
                listUserButton.setMaxWidth(Double.MAX_VALUE);

                HBox.setHgrow(editButton, Priority.ALWAYS);
                HBox.setHgrow(deleteButton, Priority.ALWAYS);
                HBox.setHgrow(listUserButton, Priority.ALWAYS);
                pane.setSpacing(10);

                deleteButton.setOnAction(event -> {
                    Conference getPatient = getTableView().getItems().get(getIndex());
                    System.out.println(getPatient.getId() + "   " + getPatient.getLocation());
                    getPatient.setDelete(true);
                    conferenceRepository.save(getPatient);
                    processConference(tableViews);
                });

                editButton.setOnAction(event -> {
                    Conference getPatient = getTableView().getItems().get(getIndex());
                    System.out.println(getPatient.getId() + "   " + getPatient.getLocation());
                    Conference conference = conferenceRepository.findById(getPatient.getId());


                });

                listUserButton.setOnAction(event -> {
                    Conference getPatient = getTableView().getItems().get(getIndex());
                    System.out.println(getPatient.getId() + "   " + getPatient.getLocation());
                    List<Accounts_Conferences> accounts = getPatient.getRegistrations();
                    System.out.println(accounts);


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

        tableViews.setRowFactory(tv -> {
            TableRow row = new TableRow<Conference>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {

                    Conference clickedRow = (Conference) row.getItem();
                    printRow(clickedRow);
                }
            });
            return row;
        });

    }

    private void printRow(Conference item) {
        System.out.println(item.toString());
    }
}
