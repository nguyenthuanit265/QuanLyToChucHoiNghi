package com.javafx;


import com.javafx.controller.UIController;
import com.javafx.entity.Role;
import com.javafx.repository.RoleRepository;
import com.javafx.repository.TestRepository;
import com.javafx.repository.impl.RoleRepositoryImpl;
import com.javafx.repository.impl.TestRepositoryImpl;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.event.ActionEvent;

import java.util.List;

@Configuration
@ComponentScan(basePackages = {"com"})
public class Main extends Application {
    public static Stage stage = null;
    private double x, y;
    private ConfigurableApplicationContext springContext;
    //    private RoleRepository roleRepository = new RoleRepositoryImpl();
    FXMLLoader fxmlLoader = null;
    Parent root;

    @FXML
    Button btnConference;

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(Main.class);
        springContext
                .getAutowireCapableBeanFactory()
                .autowireBeanProperties(
                        this,
                        AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE,
                        true
                );
        springContext.getBean(RoleRepository.class);

    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        loadHomeView(primaryStage);
    }

    public void loadHomeView(Stage primaryStage) throws java.io.IOException {
        //        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        fxmlLoader = new FXMLLoader(getClass().getResource("/admin.fxml"));
//        fxmlLoader.setControllerFactory(springContext::getBean);
        root = fxmlLoader.load();

        primaryStage.setScene(new Scene(root));

//        primaryStage.initStyle(StageStyle.UNDECORATED);

        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {

            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);

        });
        primaryStage.show();

//        UIController mainController = fxmlLoader.getController();
//        mainController.findAllConference();
    }

//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("/admin.fxml"));
//        primaryStage.setScene(new Scene(root));
//        primaryStage.initStyle(StageStyle.UNDECORATED);
//        this.stage = primaryStage;
//        stage.show();
//    }

    public static void main(String[] args) {
        launch(args);

    }
}
