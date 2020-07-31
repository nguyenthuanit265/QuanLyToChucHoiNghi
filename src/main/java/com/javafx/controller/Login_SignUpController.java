package com.javafx.controller;

import com.javafx.Main;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class Login_SignUpController extends Application implements Initializable {
    private Stage primaryStage;
    private double x, y;
    Parent root;

    @FXML
    Button btnClose;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    public Login_SignUpController() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sign_up.fxml"));

        root = fxmlLoader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
//        stage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Sign Up");


        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {

            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);

        });


        this.primaryStage = primaryStage;

        System.out.println("start method Login_SignUp Method");
        System.out.println("stage :" + this.primaryStage);
        this.primaryStage.show();
    }

    public void handleSubmitButtonAction(ActionEvent actionEvent) throws IOException {
        Window owner = submitButton.getScene().getWindow();
        if (nameField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your name");
            return;
        }
        if (emailField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your email id");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a password");
            return;
        }

        AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, "Registration Successful!",
                "Welcome " + nameField.getText());
    }

    public void close() {
        System.out.println("stage :" + this.primaryStage);
        this.primaryStage.close();
    }

    @FXML
    public void closeWindow() {

//        this.btnClose = (Button) root.lookup("#btnClose");
//
        Stage stage = (Stage) btnClose.getScene().getWindow();
        // do what you have to do
        stage.close();

//        Stage stage = new Stage();
//        stage.setScene(new Scene(root));
//
//        stage.close();


    }

    public void formLogin(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Sign in");
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

    public void closeIfLogggedIn() {
        Preferences userPreferences = Preferences.userRoot();
        String emailLoggedIn = userPreferences.get("email", "");
        String roleName = userPreferences.get("role", "");
        System.out.println("===================================>>>>>>>>>>" + emailLoggedIn.length());
        if (emailLoggedIn.length() > 0) {
            closeWindow();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
