package com.javafx.controller;

import com.javafx.entity.Location;
import com.javafx.entity.Location;
import com.javafx.entity.Role;
import com.javafx.repository.LocationRepository;
import com.javafx.repository.LocationRepository;
import com.javafx.repository.impl.LocationRepositoryImpl;
import com.javafx.repository.impl.LocationRepositoryImpl;
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

public class LocationController {
    LocationRepository locationRepository = new LocationRepositoryImpl();

    public LocationController() {
        String hashed = BCrypt.hashpw("123456", BCrypt.gensalt());
//        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
//        LocationRepository.save(new Location("test@gmai.com", "test", hashed, 1, 1));
    }

    public void processLocation(TableView<Object> tableViews) {


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
                    processLocation(tableViews);
                });

                editButton.setOnAction(event -> {
                    Location getPatient = getTableView().getItems().get(getIndex());
                    System.out.println(getPatient.getId() + "   " + getPatient.getAddress());
                    Location Location = locationRepository.findById(getPatient.getId());

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
}
