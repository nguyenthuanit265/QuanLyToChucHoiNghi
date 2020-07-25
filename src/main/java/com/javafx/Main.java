package com.javafx;

import com.javafx.config.HibernateConfig;

import com.javafx.controller.MainController;
import com.javafx.controller.UIController;
import com.javafx.entity.Role;
import com.javafx.repository.RoleRepository;
import com.javafx.repository.TestRepository;
import com.javafx.repository.impl.RoleRepositoryImpl;
import com.javafx.repository.impl.TestRepositoryImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

import java.util.List;


//@Configuration
//@ComponentScan("com")
@Configuration
@ComponentScan(basePackages = {"com"})
public class Main extends Application {
    public static Stage stage = null;
    private double x, y;
    private ConfigurableApplicationContext springContext;
    //    private RoleRepository roleRepository = new RoleRepositoryImpl();
    FXMLLoader fxmlLoader = null;
    Parent root;

//    @Autowired
//    UIController controller;

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


//        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        fxmlLoader = new FXMLLoader(getClass().getResource("/admin.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        root = fxmlLoader.load();

        primaryStage.setScene(new Scene(root));
        //set stage borderless
        primaryStage.initStyle(StageStyle.UNDECORATED);

        //drag it here
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {

            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);

        });
        primaryStage.show();

        UIController mainController = fxmlLoader.getController();
        mainController.populateTableViewBooks();

    }

//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("/admin.fxml"));
//        primaryStage.setScene(new Scene(root));
//        primaryStage.initStyle(StageStyle.UNDECORATED);
//        this.stage = primaryStage;
//        stage.show();
//    }


    @Override
    public void stop() throws Exception {
        springContext.stop();
    }


    public static void main(String[] args) {
        launch(args);

    }
}
