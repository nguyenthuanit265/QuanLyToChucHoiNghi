package com.javafx.controller;

import com.javafx.entity.Account;
import com.javafx.entity.Conference;
import com.javafx.entity.Role;
import com.javafx.repository.ConferenceRepository;
import com.javafx.repository.impl.ConferenceRepositoryImpl;
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

public class ConferenceController {

    private ConferenceRepository conferenceRepository = new ConferenceRepositoryImpl();

    public void processConference(TableView<Object> tableViews) {
        tableViews.getColumns().clear();
        List<Conference> conferences = conferenceRepository.findAllActive();
//        tableViews.setFixedCellSize(25);
//        tableViews.prefHeightProperty().bind(Bindings.size(tableViews.getItems()).multiply(tableViews.getFixedCellSize()).add(30));

        for (Conference conference : conferences) {
            System.out.println(conference.toString());

            final ObservableList<Object> data = FXCollections.observableArrayList(conferences);

            TableColumn numberCol = new TableColumn("#");
            numberCol.setMinWidth(20);
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
            idCol.setMinWidth(100);
            idCol.setCellValueFactory(
                    new PropertyValueFactory<Conference, Integer>("id"));

            TableColumn nameCol = new TableColumn("Name");
            nameCol.setMinWidth(100);
            nameCol.setCellValueFactory(
                    new PropertyValueFactory<Conference, String>("name"));

            TableColumn summaryCol = new TableColumn("Summary");
            summaryCol.setMinWidth(100);
            summaryCol.setCellValueFactory(
                    new PropertyValueFactory<Conference, String>("descriptionSummary"));

            TableColumn timeCol = new TableColumn("Time Start");
            timeCol.setMinWidth(100);
            timeCol.setCellValueFactory(
                    new PropertyValueFactory<Conference, String>("timeStart"));

            TableColumn locationCol = new TableColumn("Location");
            locationCol.setMinWidth(100);
            locationCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Conference, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Conference, String> c) {
                    return new SimpleStringProperty(c.getValue().getLocation().getAddress());
                }
            });


            tableViews.getColumns().addAll(numberCol, idCol, nameCol, summaryCol, timeCol, locationCol);

            TableColumn actionCol = new TableColumn<Role, Void>("Action");
            actionCol.setMinWidth(130);
//        actionCol.setCellValueFactory(new PropertyValueFactory<Role, Role>("id"));
            actionCol.setCellFactory(param -> new TableCell<Conference, Void>() {
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
}
