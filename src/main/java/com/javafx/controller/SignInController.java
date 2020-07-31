package com.javafx.controller;

import com.javafx.Main;
import com.javafx.entity.Account;
import com.javafx.repository.AccountRepository;
import com.javafx.repository.impl.AccountRepositoryImpl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class SignInController implements Initializable {
    private double x, y;
    @FXML
    TextField email;

    @FXML
    PasswordField password;

    @FXML
    StackPane stackPane;

    @FXML
    Button loginButton;

    @FXML
    Label lblError;

    Login_SignUpController login_signUpController = new Login_SignUpController();

    AccountRepository accountRepository = null;

    Stage stage;
    @Autowired
    private AuthenticationManager authManager;

    public void postLogin(ActionEvent actionEvent) throws Exception {

        String errorMessage;
        System.out.println(email.getText());
        System.out.println(password.getText());


        if (email.getText().trim().isEmpty() || password.getText().trim().isEmpty()) {
            errorMessage = "Email và mật khẩu không được trống";
            validateFormLogin(errorMessage);
            return;

        }
        boolean valid = EmailValidator.getInstance().isValid(email.getText());
        System.out.println(valid);
        if (valid == false) {
            errorMessage = "Email không đúng định dạng";
            validateFormLogin(errorMessage);
            return;
        }


        if (checkLogin() == false) {
            errorMessage = "Kiểm tra emaik, mật khẩu";
            validateFormLogin(errorMessage);
            return;
        } else {
            System.out.println("AFTER LOGIN CHECK");
            Preferences userPreferences = Preferences.userRoot();
            String emailLoggedIn = userPreferences.get("email", "");
            String roleName = userPreferences.get("role", "");
            System.out.println(emailLoggedIn);
            System.out.println(roleName);

//            reload after login
//            Main main = new Main();
//            System.out.println("===========>>>>>>>>>>>>>>>>>>>>>");
//            main.start(null);


//            Authentication request = new UsernamePasswordAuthenticationToken(emailLoggedIn, password);
//            Authentication result = authManager.authenticate(request);
//            SecurityContextHolder.getContext().setAuthentication(result);


            closeFormLogin();

            login_signUpController.closeIfLogggedIn();

        }


    }

    private void updateUserInfo() {

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//
//        private void closeFormLogin() throws IOException {
//        validateFormLogin("success");
    }

    public boolean checkLogin() {
        Account account = accountRepository.findByEmail(email.getText());
        if (account == null) {
            return false;
        } else {

            System.out.println(account.toString());
            if (BCrypt.checkpw(password.getText(), account.getPassword())) {
                System.out.println("It matches");
                Preferences userLogin = Preferences.userRoot();
                userLogin.put("email", account.getEmail());
                userLogin.put("role", account.getRole().getName());
                return true;
            } else {
                System.out.println("It does not match");
                return false;
            }

        }

    }


    public void validateFormLogin(String errorMessage) throws IOException {

        if (errorMessage.equals("success")) {
            closeFormLogin();
        } else {
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
            lblError.setText(errorMessage);
            stage.getScene().setRoot(root1);
        }


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        accountRepository = new AccountRepositoryImpl();
    }


    @FXML
    private void closeFormLogin() {
        // get a handle to the stage
        Stage stage = (Stage) loginButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
