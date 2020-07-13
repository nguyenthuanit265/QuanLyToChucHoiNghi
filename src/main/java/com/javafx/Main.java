package com.javafx;

import com.javafx.config.HibernateConfig;
import com.javafx.controller.Controller;
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

import java.util.List;


//@Configuration
//@ComponentScan("com")
@Configuration
@ComponentScan(basePackages = {"com"})
public class Main extends Application {
    private ConfigurableApplicationContext springContext;
    //    private RoleRepository roleRepository = new RoleRepositoryImpl();
    FXMLLoader fxmlLoader = null;
    Parent root;

    @Autowired
    Controller controller;

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
//        fxmlLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
//        fxmlLoader.setControllerFactory(springContext::getBean);
//        root = fxmlLoader.load();
//        springContext.getBean(TestRepository.class);
//
//        springContext.getBean(TestRepositoryImpl.class);
//        springContext.getBean(UserAccountRepository.class);
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

        controller.populateTableViewBooks();

    }


    @Override
    public void stop() throws Exception {
        springContext.stop();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
