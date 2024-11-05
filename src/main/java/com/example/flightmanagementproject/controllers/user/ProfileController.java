package com.example.flightmanagementproject.controllers.user;

import com.example.flightmanagementproject.auth.AuthService;
import com.example.flightmanagementproject.dao.CustomerDao;
import com.example.flightmanagementproject.dao.FlightsDao;
import com.example.flightmanagementproject.models.Customer;
import com.example.flightmanagementproject.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class ProfileController {

    @FXML
    private ImageView profilePic;

    @FXML
    private Label name;

    @FXML
    private Label availableFlightsLabel;
    @FXML
    private Label todayFlightsLabel;
    @FXML
    private Label tomorrowFlightsLabel;
    @FXML
    private Label weekFlightsLabel;

    @FXML
    private Label welcome;

    private User loggedInUser;

    @FXML
    private Circle profileCircle;
    private Customer loggedInCustomer;
    private CustomerDao customerDao;
    private FlightsDao flightsDao;

    @FXML
    public void initialize() throws SQLException {
        customerDao = new CustomerDao();
        flightsDao = new FlightsDao();
        loggedInUser = AuthService.getLoggedInUser();
        System.out.println("logged in user id : "+loggedInUser.getId());

        loggedInCustomer = customerDao.getCustomerByUser(loggedInUser);
        System.out.println("logged in customer id  : "+loggedInCustomer.getCustomerID());
        displayProfilePicture();
        name.setText(loggedInUser.getUsername());
        availableFlightsLabel.setText("Total Available Flights : "+flightsDao.getTotalFlightsCount());
        todayFlightsLabel.setText("Today's Available Flights : "+flightsDao.getFlightsCountForToday());
        weekFlightsLabel.setText("This Week's Available Flights : "+flightsDao.getFlightsCountForThisWeek());
        tomorrowFlightsLabel.setText("Tomorrow's Available Flights : "+flightsDao.getFlightsCountForTomorrow());

        welcome.setText("Welcome : "+loggedInCustomer.getName());

    }

    private void displayProfilePicture() {
        String projectDirectory = System.getProperty("user.dir");
        Image profileImage = new Image(projectDirectory + "/profile-photos/"+loggedInCustomer.getPicture());
        profileCircle.setFill(new ImagePattern(profileImage));
    }

    private User getLoggedInUser() {
        return AuthService.getLoggedInUser(); // Replace with your authentication service method
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
            name.getParent().getScene().getWindow().hide();
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
        profileCircle.getParent().getScene().getWindow().hide();
    }

    @FXML
    public void navigateToBookings() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/user/user-bookings.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        profileCircle.getParent().getScene().getWindow().hide();
    }

    @FXML
    public void navigateToEditProfile() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/user/edit-profile.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        profileCircle.getParent().getScene().getWindow().hide();
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
