package com.example.flightmanagementproject.controllers.user;

import com.example.flightmanagementproject.auth.AuthService;
import com.example.flightmanagementproject.dao.ComplaintDao;
import com.example.flightmanagementproject.dao.CustomerDao;
import com.example.flightmanagementproject.dao.FlightsDao;
import com.example.flightmanagementproject.models.Customer;
import com.example.flightmanagementproject.models.User;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

public class UserComplaintsController {
    @FXML
    private Label name;

    private User loggedInUser;

    @FXML
    private Circle profileCircle;
    private Customer loggedInCustomer;
    private CustomerDao customerDao;
    private FlightsDao flightsDao;

    @FXML
    private JFXComboBox<String> typeComboBox;

    @FXML
    private JFXTextArea descField;

    @FXML
    private TextField titleField;

    private ComplaintDao complaintDao;


    @FXML
    public void initialize() throws SQLException {
        customerDao = new CustomerDao();
        flightsDao = new FlightsDao();
        complaintDao = new ComplaintDao();
        loggedInUser = AuthService.getLoggedInUser();
        System.out.println("logged in user id : "+loggedInUser.getId());

        loggedInCustomer = customerDao.getCustomerByUser(loggedInUser);
        System.out.println("logged in customer id  : "+loggedInCustomer.getCustomerID());
        displayProfilePicture();
        name.setText(loggedInUser.getUsername());
        typeComboBox.getItems().addAll("Refund Request", "Technical Issue", "Account Support", "General Complaint");


    }

    @FXML
    public void addComplaint(ActionEvent actionEvent) {
        String type = typeComboBox.getValue();
        String title = titleField.getText();
        String description = descField.getText();

        if (type == null || title.isEmpty() || description.isEmpty()) {
            showAlert("Error", "All fields must be filled", Alert.AlertType.ERROR);
            return;
        }

        complaintDao.addComplaint(loggedInCustomer , type, title, description);

        showAlert("Success", "Complaint submitted successfully", Alert.AlertType.INFORMATION);
        clearFields();
    }

    private void displayProfilePicture() {
        String projectDirectory = System.getProperty("user.dir");
        Image profileImage = new Image(projectDirectory + "/profile-photos/"+loggedInCustomer.getPicture());
        profileCircle.setFill(new ImagePattern(profileImage));
    }

    @FXML
    public void goToDashboard(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/user/user-dashboard.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        name.getParent().getScene().getWindow().hide();
    }
    @FXML
    public void goToBookFlights(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/user/user-flights.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        name.getParent().getScene().getWindow().hide();
    }

    @FXML
    public void navigateToBookings() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/user/user-bookings.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        name.getParent().getScene().getWindow().hide();
    }

    @FXML
    public void navigateToEditProfile() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/user/edit-profile.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        name.getParent().getScene().getWindow().hide();
    }

    @FXML
    public void logout(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Logout Confirmation");
        alert.setContentText("Are you sure you want to log out?");

        alert.initModality(Modality.APPLICATION_MODAL);

        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (result == ButtonType.OK) {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/login.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            name.getParent().getScene().getWindow().hide();
        }
    }

    private void clearFields() {
        typeComboBox.setValue(null);
        titleField.clear();
        descField.clear();
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
