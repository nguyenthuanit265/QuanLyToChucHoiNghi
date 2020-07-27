package com.javafx.controller;

import com.javafx.Main;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.context.annotation.Primary;

import java.io.IOException;

public class SignUpController extends Application {
    private Stage primaryStage;
    private double x, y;
    @FXML
    Button btnBack;

    public SignUpController() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sign_up.fxml"));

        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    public void handleSubmitButtonAction(ActionEvent actionEvent) throws IOException {


    }

    public void homePage(ActionEvent actionEvent) throws Exception {
        Main main = new Main();
    }
}
