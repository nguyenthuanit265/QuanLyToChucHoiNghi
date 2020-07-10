package com.javafx;

import com.javafx.config.HibernateConfig;
import com.javafx.controller.Controller;
import com.javafx.repository.RoleRepository;
import com.javafx.repository.impl.RoleRepositoryImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.SpringApplication;


@SpringBootConfiguration
public class Main extends Application {
    private ConfigurableApplicationContext springContext;
    //    private RoleRepository roleRepository = new RoleRepositoryImpl();
    FXMLLoader fxmlLoader = null;
    Parent root;

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
//        springContext.getBean(HibernateConfig.class);
//        springContext.getBean(RoleRepositoryImpl.class);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();

        fxmlLoader = new FXMLLoader(getClass().getResource("/sample.fxml"));
//        fxmlLoader.setControllerFactory(springContext::getBean);
        root = fxmlLoader.load();

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(720.0);
        primaryStage.setMinHeight(600.0);
        primaryStage.show();

        Controller mainController = new Controller();
//        mainController.populateTableViewBooks();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
