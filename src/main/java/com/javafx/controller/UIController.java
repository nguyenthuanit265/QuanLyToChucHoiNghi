package com.javafx.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

//import com.javafx.Launch;
import com.javafx.entity.*;
import com.javafx.repository.BookRepository;
import com.javafx.repository.ConferenceRepository;
import com.javafx.repository.LocationRepository;
import com.javafx.repository.impl.BookRepositoryImpl;
import com.javafx.repository.impl.ConferenceRepositoryImpl;
import com.javafx.repository.impl.LocationRepositoryImpl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class UIController {
    public TableColumn<Book, String> col_name;
    public TableColumn<Book, String> col_category;
    public TableColumn<Book, String> col_author;
    public TableColumn<Book, String> col_publisher;
    public TableColumn<Book, String> col_yearpublished;
    public TableColumn<Book, String> col_isbn;

    @FXML
    private BorderPane mainBorderPane;

    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private AnchorPane parent;

    @FXML
    private TableView<Object> tableViews;
    private ObservableList<Book> listBooks;


    @FXML
    Button btnSignUp;


    ConferenceRepository conferenceRepository = new ConferenceRepositoryImpl();

    LocationRepository locationRepository = new LocationRepositoryImpl();

    BookRepository bookRepository = new BookRepositoryImpl();
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
//        tableViews = new TableView<>();
        RoleController roleController = new RoleController();
        roleController.processRole(tableViews);
    }


    public void findAllConference(ActionEvent actionEvent) {
//        List<Conference> cons = conferenceRepository.findAllActive();
//        for (Conference conference : cons) {
//            System.out.println(conference.toString());
//        }
        ConferenceController conferenceController = new ConferenceController();
        conferenceController.processConference(tableViews);
    }

    public void findAllLocation(ActionEvent actionEvent) {
        List<Location> locations = locationRepository.findAllActive();
        for (Location location : locations) {
            System.out.println(location.toString());
        }
    }

    public void populateTableViewBooks() {
        //txtfldSearchBook.setText(Double.toString(btnAddBook.getHeight()));
        final ObservableList<Object> data =
                FXCollections.observableArrayList(
                        new Person("Jacob", "Smith", "jacob.smith@example.com"),
                        new Person("Isabella", "Johnson", "isabella.johnson@example.com"),
                        new Person("Ethan", "Williams", "ethan.williams@example.com"),
                        new Person("Emma", "Jones", "emma.jones@example.com"),
                        new Person("Michael", "Brown", "michael.brown@example.com")
                );


        if (listBooks != null && listBooks.size() != 0) {
            listBooks.removeAll();
        }

        List<Book> books = bookRepository.findAll();
        listBooks = FXCollections.observableList(books);


        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("firstName"));

        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setMinWidth(100);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("lastName"));

        TableColumn emailCol = new TableColumn("Email");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("email"));

        tableViews.setItems(data);
        tableViews.getColumns().addAll(firstNameCol, lastNameCol, emailCol);


//        tableViews.getColumns().addAll(firstNameCol, lastNameCol, emailCol);
//        tableViews.setItems(listBooks);
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

        populateTableViewBooks();
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
        populateTableViewBooks();
    }

    public void refreshBookList(ActionEvent actionEvent) {
        populateTableViewBooks();
    }

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

        populateTableViewBooks();
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
        AccountController accountController = new AccountController();

        accountController.ProcessAccount(tableViews);
    }

    public void SignUp(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sign_up.fxml"));
        Stage stage = (Stage) btnSignUp.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
