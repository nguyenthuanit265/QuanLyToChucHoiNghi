package com.javafx.controller;

import com.javafx.entity.*;
import com.javafx.repository.Accounts_ConferencesRepository;
import com.javafx.repository.ConferenceRepository;
import com.javafx.repository.impl.Accounts_ConferencesRepositoryImpl;
import com.javafx.repository.impl.ConferenceRepositoryImpl;
import com.javafx.session.AccountLoginSession;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.mindrot.jbcrypt.BCrypt;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ConferenceController implements Initializable {

    @FXML
    TextField txtIdConference;

    @FXML
    TextField txtNameConference;

    @FXML
    TextField txtDescriptionSummary;

    @FXML
    TextField txtDescriptionDetail;

    @FXML
    TextArea txtLocation;

    @FXML
    TextField txtTimeStart;

    @FXML
    TextField txtTimeEnd;

    @FXML
    ImageView imgConference;

    @FXML
    TableView<Object> tableViewListUser;

    @FXML
    Button btnRegisterConference;

    AccountLoginSession accountLoginSession;

    Accounts_ConferencesRepository accounts_conferencesRepository = new Accounts_ConferencesRepositoryImpl();


    private double x, y;
    private ConferenceRepository conferenceRepository = new ConferenceRepositoryImpl();


    public void processConference(TreeView<Object> treeViews) {
        List<Conference> conferences = conferenceRepository.findAllActive();
        BookCategory catRoot = new BookCategory("ROOT", "Root");
        // Phần tử gốc
        TreeItem rootItems = new TreeItem();
        rootItems = new TreeItem<>(catRoot);
        rootItems.setExpanded(true);

        TreeItem itemId = new TreeItem();
        for (int i = 0; i < conferences.size(); i++) {
            System.out.println("size: " + conferences.size());
            itemId.setValue(conferences.get(i).getId());
            TreeItem toStringRole = new TreeItem(conferences.get(i).toString());

            itemId.getChildren().addAll(toStringRole);

            System.out.println(itemId);
            rootItems.getChildren().addAll(itemId);
            itemId = new TreeItem();
        }


        treeViews.setRoot(rootItems);

        // Ẩn phần tử gốc.
        treeViews.setShowRoot(false);

    }


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

        accountLoginSession = new AccountLoginSession();
        String roleNameAccount = accountLoginSession.getRoleAccountLogin();
        if (roleNameAccount == null) {
            CustomButtonForUser(tableViews);
        } else if (roleNameAccount.equals("ROLE_ADMIN")) {
            CustomButtonForAdmin(tableViews);
        } else {
            CustomButtonForUserLoggedIn(tableViews);
        }
        tableViews.setItems(data);

        tableViews.setRowFactory(tv -> {
            TableRow row = new TableRow<Conference>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {

                    Conference clickedRow = (Conference) row.getItem();
                    try {
                        printRow(clickedRow);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });

    }

    public void CustomButtonForAdmin(TableView<Object> tableViews) {
        TableColumn actionCol = new TableColumn<Conference, Void>("Action");
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
    }

    public void CustomButtonForUser(TableView<Object> tableViews) {
        TableColumn actionCol = new TableColumn<Conference, Void>("Action");
        actionCol.setMinWidth(150);
//        actionCol.setCellValueFactory(new PropertyValueFactory<Role, Role>("id"));
        actionCol.setCellFactory(param -> new TableCell<Conference, Void>() {

            private final Button listUserButton = new Button("List User");

            private final HBox pane = new HBox(listUserButton);

            {
                listUserButton.setStyle("-fx-background-color: #d9455f; -fx-text-fill: white; -fx-cursor: hand;");
                listUserButton.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(listUserButton, Priority.ALWAYS);
                pane.setSpacing(10);
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
    }


    public void CustomButtonForUserLoggedIn(TableView<Object> tableViews) {
        accountLoginSession = new AccountLoginSession();
        Account account = accountLoginSession.getAccountLogin();
        TableColumn actionCol = new TableColumn<Conference, Void>("Action");
        actionCol.setMinWidth(150);
//        actionCol.setCellValueFactory(new PropertyValueFactory<Role, Role>("id"));
        actionCol.setCellFactory(param -> new TableCell<Conference, Void>() {

            private final Button cancelButton = new Button("Cancel");
            private final Button listUserButton = new Button("List User");

            private final HBox pane = new HBox(cancelButton, listUserButton);

            {

                cancelButton.setStyle("-fx-background-color: #d9455f; -fx-text-fill: white; -fx-cursor: hand;");
                listUserButton.setStyle("-fx-background-color: #d9455f; -fx-text-fill: white; -fx-cursor: hand;");


                cancelButton.setMaxWidth(Double.MAX_VALUE);
                listUserButton.setMaxWidth(Double.MAX_VALUE);


                HBox.setHgrow(cancelButton, Priority.ALWAYS);
                HBox.setHgrow(listUserButton, Priority.ALWAYS);
                pane.setSpacing(10);


                cancelButton.setOnAction(event -> {
                    Conference getPatient = getTableView().getItems().get(getIndex());
                    System.out.println(getPatient.getId() + "   " + getPatient.getLocation());

//                    getPatient.setDelete(true);
//                    conferenceRepository.save(getPatient);
//                    Lấy ra danh sách các hội nghị mà user đã tham gia
                    List<Accounts_Conferences> accounts_conferences = account.getRegistrations();
//                    Duyệt để kiểm tra hội nghị mà user muốn hủy có đăng ký chưa
                    for (Accounts_Conferences item : accounts_conferences) {
                        if (item.getConference().getId() == getPatient.getId()) {
                            System.out.println("=========================>>>>>>>>>>>>>>>>>> Hội nghị này đang tham gia");
                            Date now = new Date();
                            if (now.before(item.getConference().getTimeStartEvent())) {
                                System.out.println("===================>>>>>>>>>>>>>>>>>>>> Canceled");
                                item.setCancel(true);

                                accounts_conferencesRepository.save(item);

                            } else {
                                System.out.println("===================>>>>>>>>>>>>>>>>>>>> Not Cancel");
                                System.out.println("===================>>>>>>>>>>>>>>>>>>>> Because Event completed or happening");
                            }
                            break;
                        }
                    }


                    processConference(tableViews);
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
    }

    private void printRow(Conference item) throws IOException {
        System.out.println(item.toString());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/conference_detail.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Thông tin chi tiết hội nghị");
        stage.setScene(new Scene(root1));

        root1.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root1.setOnMouseDragged(event -> {

            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);

        });


        txtIdConference = (TextField) root1.lookup("#txtIdConference");
        txtIdConference.setText(String.valueOf(item.getId()));


        txtNameConference = (TextField) root1.lookup("#txtNameConference");
        txtNameConference.setText(item.getName());

        txtDescriptionSummary = (TextField) root1.lookup("#txtDescriptionSummary");
        txtDescriptionSummary.setText(item.getDescriptionSummary());

        txtDescriptionDetail = (TextField) root1.lookup("#txtDescriptionDetail");
        txtDescriptionDetail.setText(item.getDescriptionDetail());

        txtLocation = (TextArea) root1.lookup("#txtLocation");
        txtLocation.setText(item.getLocation().getAddress());

        txtTimeStart = (TextField) root1.lookup("#txtTimeStart");
        txtTimeStart.setText(item.getTimeEndEvent().toString());


        txtTimeEnd = (TextField) root1.lookup("#txtTimeEnd");
        txtTimeEnd.setText(item.getTimeStartEvent().toString());


        imgConference = (ImageView) root1.lookup("#imgConference");
        imgConference.setImage(new Image(item.getImage()));

        showListAccount(tableViewListUser, item, root1);


        stage.show();


    }

    private void showListAccount(TableView tableViewListUser, Conference conference, Parent root1) {

        tableViewListUser = (TableView) root1.lookup("#tableViewListUser");
        tableViewListUser.setEditable(true);
        tableViewListUser.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


//        Lấy ra danh sách các User đang tham gia hội nghị
        List<Accounts_Conferences> accounts_conferences = conference.getRegistrations();
        System.out.println(accounts_conferences);
        List<Account> accounts = new ArrayList<>();
        for (Accounts_Conferences temp : accounts_conferences) {
            if (temp.isCancel() == false) {
                accounts.add(temp.getAccount());
            }
        }

        System.out.println(accounts);

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


        tableViewListUser.getColumns().addAll(numberCol, idCol, usernameCol, emailCol);

//        TableColumn actionCol = new TableColumn<Conference, Void>("Action");
//        actionCol.setMinWidth(100);
//
//        actionCol.setCellFactory(param -> new TableCell<Conference, Void>() {
//
//            private final Button cancelButton = new Button("Cancel");
//
//            private final HBox pane = new HBox(cancelButton);
//
//            {
//
//                cancelButton.setStyle("-fx-background-color: #d9455f; -fx-text-fill: white;-fx-cursor: hand;");
//                cancelButton.setMaxWidth(Double.MAX_VALUE);
//                HBox.setHgrow(cancelButton, Priority.ALWAYS);
//                pane.setSpacing(10);
//
//
//                cancelButton.setOnAction(event -> {
//                    Conference getPatient = getTableView().getItems().get(getIndex());
//                    System.out.println(getPatient.getId() + "   " + getPatient.getName());
//                });
//
//            }
//
//            @Override
//            protected void updateItem(Void item, boolean empty) {
//                super.updateItem(item, empty);
//
//                setGraphic(empty ? null : pane);
//            }
//        });
//
//
//        tableViewListUser.getColumns().add(actionCol);
        tableViewListUser.setItems(data);


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtNameConference.setText("");
        txtDescriptionSummary.setText("");
    }

    public void registerConference(ActionEvent actionEvent) {
        accountLoginSession = new AccountLoginSession();
        Account account = accountLoginSession.getAccountLogin();
        if (account == null) {
            System.out.println("Account not login");
        } else {
            System.out.println("================>>>>>>> Account Login: ");
            System.out.println(account.toString());
            System.out.println("================>>>>>>> ID Conference Register: " + txtIdConference.getText());
            accounts_conferencesRepository = new Accounts_ConferencesRepositoryImpl();


        }
    }
}
