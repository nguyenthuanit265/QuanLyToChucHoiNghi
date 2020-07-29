package com.javafx.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

//import com.javafx.Launch;
import com.javafx.entity.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;


@Component
public class UIController implements Initializable {
    public TableColumn<Book, String> col_name;
    public TableColumn<Book, String> col_category;
    public TableColumn<Book, String> col_author;
    public TableColumn<Book, String> col_publisher;
    public TableColumn<Book, String> col_yearpublished;
    public TableColumn<Book, String> col_isbn;

    @FXML
    private BorderPane mainBorderPane;
    private double x, y;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private ImageView chartImage;

    @FXML
    private HBox homeContent;

    @FXML
    private AnchorPane contentBodyAnchorPane;

    @FXML
    Pane paneLayoutHome;

    private TableView<Object> tableViews;

//    private TreeItem<Object> rootItems;

    private TreeView<Object> treeViews;

    private ObservableList<Book> listBooks;


    @FXML
    Button btnSignUp;

    @FXML
    Label lblTitle;

    @FXML
    ToggleGroup group;

    @FXML
    RadioButton radioButtonTableView;

    @FXML
    RadioButton radioButtonTreeView;

    ActionEvent actionEvent;

    int chooseView = 1;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("init");
//        group = new ToggleGroup();
        radioButtonTableView.setToggleGroup(group);
        radioButtonTreeView.setToggleGroup(group);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @SneakyThrows
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {

                // Có lựa chọn
                if (group.getSelectedToggle() != null) {
                    RadioButton button = (RadioButton) group.getSelectedToggle();
                    System.out.println("Button: " + button.getText());
                    if (button.getText().equals("TableView")) {
                        chooseView = 0;
                    } else {
                        chooseView = 1;
                    }
                    loadBody();

                }
            }
        });

    }

    public void loadBody() throws IOException {
        final Node source = (Node) actionEvent.getSource();
        String id = source.getId();

        switch (id) {
            case "roles":
                findAllRole(actionEvent);
                break;
            case "accounts":
                findAllAccount(actionEvent);
                break;
            case "locations":
                findAllLocation(actionEvent);
                break;
            case "conferences":
                findAllConference(actionEvent);
                break;
            default:
                loadHomePage(actionEvent);

        }


    }

    public void fitTableView() {
        tableViews = new TableView<>();
        tableViews.setPrefWidth(1200.0);
//        tableViews.setPrefHeight(600.0);
        tableViews.setEditable(true);
        tableViews.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        contentBodyAnchorPane.getChildren().add(tableViews);
    }

    public void fitTreeView() {
        treeViews = new TreeView<>();
        treeViews.setPrefWidth(1200.0);
        contentBodyAnchorPane.getChildren().add(treeViews);
    }
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        // TODO
//        makeStageDrageable();
//    }

//    private void makeStageDrageable() {
//        parent.setOnMousePressed(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                xOffset = event.getSceneX();
//                yOffset = event.getSceneY();
//            }
//        });
//        parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                Main.stage.setX(event.getScreenX() - xOffset);
//                Main.stage.setY(event.getScreenY() - yOffset);
//                Main.stage.setOpacity(0.7f);
//            }
//        });
//        parent.setOnDragDone((e) -> {
//            Main.stage.setOpacity(1.0f);
//        });
//        parent.setOnMouseReleased((e) -> {
//            Main.stage.setOpacity(1.0f);
//        });
//
//    }

    public void findAllRole(ActionEvent actionEvent) throws IOException {
        lblTitle.setText("Danh sách quyền truy cập");
        blockHomePage();
        this.actionEvent = actionEvent;
        final Node source = (Node) actionEvent.getSource();
        String id = source.getId();
        System.out.println("id event: " + id);

        RoleController roleController = new RoleController();

        if (chooseView == 0) {
            fitTableView();
            roleController.processRole(tableViews);
        } else {
            fitTreeView();
            roleController.processRole(treeViews);
        }
//

    }


    public void findAllConference(ActionEvent actionEvent) {
        lblTitle.setText("Danh sách hội nghị");
        blockHomePage();
        this.actionEvent = actionEvent;
        final Node source = (Node) actionEvent.getSource();
        String id = source.getId();
        System.out.println("id event: " + id);

        fitTableView();
        ConferenceController conferenceController = new ConferenceController();
        conferenceController.processConference((TableView<Object>) tableViews);
    }

    public void findAllLocation(ActionEvent actionEvent) {
        lblTitle.setText("Danh sách địa điểm hội nghị");
        blockHomePage();
        this.actionEvent = actionEvent;
        final Node source = (Node) actionEvent.getSource();
        String id = source.getId();
        System.out.println("id event: " + id);

        fitTableView();
        LocationController locationController = new LocationController();
        locationController.processLocation(tableViews);
    }


    public void addNewBook(ActionEvent actionEvent) throws IOException {
        Dialog<ButtonType> dialog = new Dialog<>();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/addbook.fxml"));
        Parent root = loader.load();
        dialog.setDialogPane((DialogPane) root);
        dialog.initOwner(mainBorderPane.getScene().getWindow());
//        dialog.initModality(Modality.WINDOW_MODAL);
//        dialog.setResizable(true);
        dialog.setTitle("Thêm 1 sách mới");

        Optional<ButtonType> result = dialog.showAndWait();

//        if (result.isPresent() && result.get() == ButtonType.OK) {
//            AddBookController controller = loader.getController();
//            Book book = controller.getNewAddedBook();
//            bookRepository.save(book);
//        }

//        populateTableViewBooks();
    }


    public void deleteBook(ActionEvent actionEvent) {
        ObservableList itemsDelete = tableViews.getSelectionModel().getSelectedItems();
        if (itemsDelete.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Xóa sách...");
            alert.setHeaderText("Chọn ít nhất một cuốn sách để xóa");
            alert.initOwner(mainBorderPane.getScene().getWindow());
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xóa sách...");
        alert.setResizable(false);
        alert.setContentText("Bạn có chắc muốn xóa những sách đã chọn?");
        alert.initOwner(mainBorderPane.getScene().getWindow());

        Optional<ButtonType> result = alert.showAndWait();
//        if (result.isPresent() && result.get() == ButtonType.OK) {
//            bookRepository.deleteAll(itemsDelete);
//        }
//        populateTableViewBooks();
    }

//    public void refreshBookList(ActionEvent actionEvent) {
//        populateTableViewBooks();
//    }

    public void clickMenuItemExit(ActionEvent actionEvent) {
//        Stage mainStage = (Stage) mainBorderPane.getScene().getWindow();
//        if (QuanLyThuVienJavafxJpaApplication.showClosingWindowDialog(mainStage)) {
//            mainStage.close();
//        }
    }

    public void onEditCommit(TableColumn.CellEditEvent<Book, String> bookStringCellEditEvent) {
        Book b = bookStringCellEditEvent.getRowValue();
        TableColumn<Book, String> column = bookStringCellEditEvent.getTableColumn();

        if (column.equals(col_name)) {
            b.setName(bookStringCellEditEvent.getNewValue());
        } else if (column.equals(col_category)) {
            b.setCategory(bookStringCellEditEvent.getNewValue());
        } else if (column.equals(col_author)) {
            b.setAuthor(bookStringCellEditEvent.getNewValue());
        } else if (column.equals(col_publisher)) {
            b.setPublisher(bookStringCellEditEvent.getNewValue());
        } else if (column.equals(col_yearpublished)) {
            b.setYearPublished(bookStringCellEditEvent.getNewValue());
        } else if (column.equals(col_isbn)) {
            b.setISBN(bookStringCellEditEvent.getNewValue());
        }

//        bookRepository.saveAndFlush(b);

//        populateTableViewBooks();
    }

    public void showAboutDialog(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About...");
        alert.setResizable(false);
        alert.setHeaderText("Library Management - JavaFX + Spring Boot + JPA");
        alert.setContentText("Credits : Minh Thuan & Khanh Hoang");
        alert.initOwner(mainBorderPane.getScene().getWindow());
        alert.showAndWait();
    }

    public void findAllAccount(ActionEvent actionEvent) {
        lblTitle.setText("Danh sách tài khoản");
        blockHomePage();
        this.actionEvent = actionEvent;
        final Node source = (Node) actionEvent.getSource();
        String id = source.getId();
        System.out.println("id event: " + id);

        AccountController accountController = new AccountController();


        if (chooseView == 0) {
            fitTableView();
            accountController.processAccount(tableViews);
        } else {
            fitTreeView();
            accountController.processAccount(treeViews);
        }

    }

    public void SignUp() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sign_up.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Sign Up");
        stage.setScene(new Scene(root1));

        root1.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root1.setOnMouseDragged(event -> {

            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);

        });

        stage.show();

    }

    public void loadHomePage(ActionEvent actionEvent) {
        lblTitle.setText("Trang chủ");
        findAllConference(actionEvent);

    }

    public void blockHomePage() {

//        chartImage.setVisible(false);
//        homeContent.setVisible(false);
//        paneLayoutHome.getChildren().clear();

    }
}
