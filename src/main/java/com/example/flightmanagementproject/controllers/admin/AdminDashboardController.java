package com.example.flightmanagementproject.controllers.admin;

import com.example.flightmanagementproject.dao.BookingDao;
import com.example.flightmanagementproject.dao.ComplaintDao;
import com.example.flightmanagementproject.dao.CustomerDao;
import com.example.flightmanagementproject.dao.FlightsDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class AdminDashboardController {
    @FXML
    private Label username;

    @FXML
    private Button btnLogout;

    @FXML
    public Label nbrofflights;

    @FXML
    public Label nbrOfCustomers;
    @FXML
    public Label nbrOfSupport;
    @FXML
    public Label nbrOfBookings;
    @FXML
    public Label revenue;


    @FXML
    private Button navFlights;
    @FXML
    private Button navCustomers;
    @FXML
    private Button navTickets;

    FlightsDao flightsDao;
    CustomerDao customerDao;
    ComplaintDao complaintDao;
    BookingDao bookingDao;


    @FXML
    private void initialize() throws URISyntaxException, SQLException {
        flightsDao = new FlightsDao();
        customerDao = new CustomerDao();
        bookingDao = new BookingDao();
        complaintDao = new ComplaintDao();
        nbrofflights.setText(String.valueOf(flightsDao.getTotalFlightsCount()));
        nbrOfCustomers.setText(String.valueOf(customerDao.getTotalCustomers()));
        nbrOfBookings.setText(String.valueOf(bookingDao.getTotalBookingsCount()));
        revenue.setText(String.format("$%.2f", bookingDao.getTotalRevenue()));
        nbrOfSupport.setText(String.valueOf(complaintDao.getTotalComplaints()));
    }

    @FXML
    public void logout() throws IOException {
        // Show confirmation dialog
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
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/login.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            btnLogout.getParent().getScene().getWindow().hide();
        }
    }

    @FXML
    public void navigateToFlights() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/admin/flights-admin.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        btnLogout.getParent().getScene().getWindow().hide();
    }




    @FXML
    public void navigateToCustomers() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/admin/customers-admin.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        btnLogout.getParent().getScene().getWindow().hide();
    }

    @FXML
    public void navigateToTickets() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/admin/admin-bookings.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        btnLogout.getParent().getScene().getWindow().hide();
    }

    @FXML
    public void navigateToSupport() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/admin/admin-support.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        btnLogout.getParent().getScene().getWindow().hide();
    }
}
