package com.javafx.controller;

import com.javafx.entity.Location;
import com.javafx.entity.Role;
import com.javafx.repository.LocationRepository;
import com.javafx.repository.impl.LocationRepositoryImpl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class LocationController {
    LocationRepository locationRepository = new LocationRepositoryImpl();

    final TextField nameLocation = new TextField();
    final TextField address = new TextField();
    final TextField capacity = new TextField();


    final TextField nameLocationEdit = new TextField();
    final TextField addressEdit = new TextField();
    final TextField capacityEdit = new TextField();

    final TextField idEdit = new TextField();


    public LocationController() {
        String hashed = BCrypt.hashpw("123456", BCrypt.gensalt());
        nameLocation.setMinWidth(300);
        address.setMinWidth(300);
        capacity.setMinWidth(50);


        nameLocationEdit.setMinWidth(300);
        addressEdit.setMinWidth(300);
        capacityEdit.setMinWidth(50);
        idEdit.setVisible(false);
//        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
//        LocationRepository.save(new Location("test@gmai.com", "test", hashed, 1, 1));
    }

    public void processLocation(TableView<Object> tableViews, HBox hBoxSave, HBox hBoxUpdate) {


        tableViews.getColumns().clear();
        tableViews.getItems().clear();


        List<Location> locations = locationRepository.findAllActive();
//        tableViews.setFixedCellSize(25);
//        tableViews.prefHeightProperty().bind(Bindings.size(tableViews.getItems()).multiply(tableViews.getFixedCellSize()).add(30));

//        for (Location role : Locations) {
//            System.out.println(role.toString());
//        }
        final ObservableList<Object> data = FXCollections.observableArrayList(locations);

        TableColumn numberCol = new TableColumn("#");
        numberCol.setMinWidth(20);
        numberCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Location, Location>, ObservableValue<Location>>() {
            @Override
            public ObservableValue<Location> call(TableColumn.CellDataFeatures<Location, Location> p) {
                return new ReadOnlyObjectWrapper(p.getValue());
            }
        });

        numberCol.setCellFactory(new Callback<TableColumn<Location, Location>, TableCell<Location, Location>>() {
            @Override
            public TableCell<Location, Location> call(TableColumn<Location, Location> param) {
                return new TableCell<Location, Location>() {
                    @Override
                    protected void updateItem(Location item, boolean empty) {
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
                new PropertyValueFactory<Location, Integer>("id"));

        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Location, String>("name"));

        TableColumn addressCol = new TableColumn("Address");
        addressCol.setMinWidth(100);
        addressCol.setCellValueFactory(
                new PropertyValueFactory<Location, String>("address"));


        TableColumn capacityCol = new TableColumn("Capacity");
        capacityCol.setMinWidth(100);
        capacityCol.setCellValueFactory(
                new PropertyValueFactory<Location, String>("capacity"));

        tableViews.getColumns().addAll(numberCol, nameCol, addressCol, capacityCol);

        TableColumn actionCol = new TableColumn<Role, Void>("Action");
        actionCol.setMinWidth(130);
//        actionCol.setCellValueFactory(new PropertyValueFactory<Role, Role>("id"));

        HBox finalHBoxAdd = hBoxSave;

        actionCol.setCellFactory(param -> new TableCell<Location, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");


            private final HBox pane = new HBox(editButton, deleteButton);

            {
                editButton.setStyle("-fx-background-color: #74d4c0; -fx-text-fill: white; -fx-cursor: hand;");
                deleteButton.setStyle("-fx-background-color: #d9455f; -fx-text-fill: white; -fx-cursor: hand;");


                editButton.setMaxWidth(Double.MAX_VALUE);
                deleteButton.setMaxWidth(Double.MAX_VALUE);

                HBox.setHgrow(editButton, Priority.ALWAYS);
                HBox.setHgrow(deleteButton, Priority.ALWAYS);

                pane.setSpacing(10);

                deleteButton.setOnAction(event -> {
                    Location getPatient = getTableView().getItems().get(getIndex());
                    System.out.println(getPatient.getId() + "   " + getPatient.getAddress());
                    getPatient.setDelete(true);
                    locationRepository.save(getPatient);
                    hBoxSave.getChildren().clear();
                    hBoxUpdate.getChildren().clear();
                    processLocation(tableViews, hBoxSave, hBoxUpdate);
                });

                editButton.setOnAction(event -> {
                    Location getPatient = getTableView().getItems().get(getIndex());
                    System.out.println(getPatient.getId() + "   " + getPatient.getAddress());
                    Location location = locationRepository.findById(getPatient.getId());

                    nameLocationEdit.setText(location.getName());
                    addressEdit.setText(location.getAddress());
                    capacityEdit.setText(String.valueOf(location.getCapacity()));
                    idEdit.setText(String.valueOf(location.getId()));


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


//        FORM ADD VS UPDATE
        addAndUpdateLocation(tableViews, hBoxSave, hBoxUpdate, nameCol, addressCol, capacityCol);


//        contentBodyAnchorPane.getChildren().add(hBoxAdd);

//        tableViews.setRowFactory(tv -> {
//            TableRow row = new TableRow();
//            row.setOnMouseClicked(event -> {
//                if (event.getClickCount() == 2 && (!row.isEmpty())) {
//                    Location rowData = (Location) row.getItem();
//                    System.out.println("Double click on: " + rowData.getUsername());
//                    System.out.println("List conference :");
//                    System.out.println(rowData.getConference());
//
//                }
//            });
//            return row;
//        });
    }

    public void addAndUpdateLocation(TableView<Object> tableViews, HBox hBoxSave, HBox hBoxUpdate, TableColumn nameCol, TableColumn addressCol, TableColumn capacityCol) {

        nameLocation.setPromptText("Name Location");
        nameLocation.setMaxWidth(nameCol.getPrefWidth());


        address.setMaxWidth(addressCol.getPrefWidth());
        address.setPromptText("Address");


        capacity.setMaxWidth(capacityCol.getPrefWidth());
        capacity.setPromptText("Capacity");


        nameLocationEdit.setPromptText("Name Location Edit");
        nameLocationEdit.setMaxWidth(nameCol.getPrefWidth());


        addressEdit.setMaxWidth(addressCol.getPrefWidth());
        addressEdit.setPromptText("Address Edit");


        capacityEdit.setMaxWidth(capacityCol.getPrefWidth());
        capacityEdit.setPromptText("Capacity Edit");


        final Button addButton = new Button("Add");
        final Button updateButton = new Button("Update");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {


                locationRepository.save(new Location(
                        nameLocation.getText(),
                        address.getText(),
                        Integer.parseInt(capacity.getText())));


                nameLocation.clear();
                address.clear();
                capacity.clear();

                hBoxSave.getChildren().clear();
                hBoxUpdate.getChildren().clear();
                processLocation(tableViews, hBoxSave, hBoxUpdate);
            }
        });

        updateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                locationRepository.save(new Location(Integer.parseInt(idEdit.getText()),
                        nameLocationEdit.getText(),
                        addressEdit.getText(),
                        Integer.parseInt(capacityEdit.getText())));


                nameLocationEdit.clear();
                addressEdit.clear();
                capacityEdit.clear();
                idEdit.clear();

                hBoxSave.getChildren().clear();
                hBoxUpdate.getChildren().clear();
                processLocation(tableViews, hBoxSave, hBoxUpdate);
            }
        });


        hBoxSave.getChildren().addAll(nameLocation, address, capacity, addButton);
        hBoxSave.setSpacing(30);


        hBoxUpdate.getChildren().addAll(nameLocationEdit, addressEdit, capacityEdit, updateButton);
        hBoxUpdate.setSpacing(30);
    }
}
