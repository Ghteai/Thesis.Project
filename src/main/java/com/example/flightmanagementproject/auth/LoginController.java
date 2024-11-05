package com.example.flightmanagementproject.auth;

import com.example.flightmanagementproject.dao.UserDao;
import com.example.flightmanagementproject.models.User;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URISyntaxException;

public class LoginController {

    @FXML
    private ImageView logo;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private JFXButton signUpButton;

    private UserDao userDao = new UserDao();

    @FXML
    private void initialize() throws URISyntaxException {
        Image img = new Image(getClass().getResource("/com/example/flightmanagementproject/images/logo_plane.png").toURI().toString());
        logo.setImage(img);

        txtPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    login();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void login() throws IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        User user = userDao.login(username, password);

        if (user != null) {
            System.out.println("LOGIN ID : "+user.getId()+"username "+user.getUsername());
            AuthService.setLoggedInUser(user);
            if (user.isAdmin()) {
                // Admin login
                showAdminWindow(user);
            } else {
                // Customer login
                showCustomerWindow(user);
            }
        } else {
            // Incorrect login
            showIncorrectLoginAlert();
        }
    }



    @FXML
    public void close() {
        Platform.exit();
    }

    private void showAdminWindow(User user) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/admin/admin-dashboard.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        txtPassword.getParent().getScene().getWindow().hide();
        System.out.println("Welcome, Admin: " + user.getUsername());
    }

    private void showCustomerWindow(User user) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/user/user-dashboard.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        txtPassword.getParent().getScene().getWindow().hide();
        System.out.println("Welcome, Customer: " + user.getUsername());
    }

    private void showIncorrectLoginAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText("Incorrect username or password. Please try again.");
        alert.showAndWait();
    }

    @FXML
    public void goToRegister() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/register.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        txtPassword.getParent().getScene().getWindow().hide();
    }
}
