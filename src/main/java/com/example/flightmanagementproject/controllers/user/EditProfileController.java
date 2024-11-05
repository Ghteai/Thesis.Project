package com.example.flightmanagementproject.controllers.user;

import com.example.flightmanagementproject.auth.AuthService;
import com.example.flightmanagementproject.dao.CustomerDao;
import com.example.flightmanagementproject.dao.UserDao;
import com.example.flightmanagementproject.models.Customer;
import com.example.flightmanagementproject.models.User;
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
import java.time.LocalDate;
import java.util.Date;

public class EditProfileController {
    @FXML
    private TextField fullNameField;


    @FXML
    private TextField passportField;

    @FXML
    private TextField nationalityField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private DatePicker dobField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField oldPasswordField;

    @FXML
    private PasswordField newPasswordField;


    private CustomerDao customerDao;
    private UserDao userDao;

    @FXML
    private Label name;

    private User loggedInUser;
    private Customer customer;

    @FXML
    private Circle profileCircle;

    @FXML
    public void initialize() throws SQLException {
        customerDao = new CustomerDao();
        userDao = new UserDao();
        loggedInUser = AuthService.getLoggedInUser();
        customer = customerDao.getCustomerByUser(loggedInUser);
        name.setText(loggedInUser.getUsername());
        displayProfilePicture();
        usernameField.setDisable(true);
        populateFields();
    }

    public void populateFields(){
        fullNameField.setText(customer.getName());
        passportField.setText(customer.getPassportNumber());
        nationalityField.setText(customer.getNationality());
        dobField.setValue(customer.getDateOfBirth());
        emailField.setText(customer.getEmail());
        phoneField.setText(customer.getPhone());
        usernameField.setText(loggedInUser.getUsername());
    }

    private void displayProfilePicture() {
        String projectDirectory = System.getProperty("user.dir");
        Image profileImage = new Image(projectDirectory + "/profile-photos/"+customer.getPicture());
        profileCircle.setFill(new ImagePattern(profileImage));
    }

    @FXML
    public void logout(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Logout Confirmation");
        alert.setContentText("Are you sure you want to log out?");

        // Modality.APPLICATION_MODAL makes sure the alert window blocks interactions with the main window
        alert.initModality(Modality.APPLICATION_MODAL);

        // Get the result of the confirmation dialog
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        // If the user clicked OK, proceed with logout
        if (result == ButtonType.OK) {
            AuthService.logout();
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/login.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            fullNameField.getParent().getScene().getWindow().hide();
        }
    }

    @FXML
    public void goToBookFlights(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/user/user-flights.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        fullNameField.getParent().getScene().getWindow().hide();
    }

    @FXML
    public void navigateToBookings() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/user/user-bookings.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        fullNameField.getParent().getScene().getWindow().hide();
    }

    @FXML
    public void goToDashboard() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/user/user-dashboard.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        fullNameField.getParent().getScene().getWindow().hide();
    }
    @FXML
    public void saveChanges(ActionEvent actionEvent) throws SQLException {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String userPass = userDao.getPasswordByUsername(usernameField.getText());

        if (oldPassword.isEmpty() && newPassword.isEmpty()) {
            // Only update non-password fields if both old and new passwords are empty
            updateNonPasswordFields();
            showAlert("Changes saved successfully!");
        } else if (!oldPassword.isEmpty() && userPass.equals(oldPassword)) {
            updatePassword(newPassword);
            showAlert("Password changed successfully!");
        } else {
            showAlert("Old password is incorrect!");
        }
    }

    private void updateNonPasswordFields() throws SQLException {
        String newFullName = fullNameField.getText();
        String newPassportNumber = passportField.getText();
        String newNationality = nationalityField.getText();
        String newPhone = phoneField.getText();
        String newEmail = emailField.getText();
        LocalDate newDate = dobField.getValue();
        customer.setName(newFullName);
        customer.setPassportNumber(newPassportNumber);
        customer.setNationality(newNationality);
        customer.setPhone(newPhone);
        customer.setEmail(newEmail);
        customer.setDateOfBirth(newDate);
        customer.setGender(customer.getGender());
        customer.setPassportExpiryDate(customer.getPassportExpiryDate());

        customerDao.updateCustomer(customer);
    }

    private void updatePassword(String newPassword) throws SQLException {
        loggedInUser.setPassword(newPassword);
        userDao.updatePassword(loggedInUser.getId(), newPassword);
        showAlert("Password changed successfully!");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void navigateToSupport() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/user/user-complaints.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        profileCircle.getParent().getScene().getWindow().hide();
    }
}
