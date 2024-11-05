package com.example.flightmanagementproject.controllers.admin;

import com.example.flightmanagementproject.auth.AuthService;
import com.example.flightmanagementproject.dao.BookingDao;
import com.example.flightmanagementproject.dao.CustomerDao;
import com.example.flightmanagementproject.dao.FlightsDao;
import com.example.flightmanagementproject.models.Booking;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class AdminBookingsController {
    @FXML
    private Button btnLogout;

    @FXML
    private TableView<Booking> bookingsTable;

    @FXML
    private TableColumn<Booking, String> referenceNumberColumn;

    @FXML
    private TableColumn<Booking, Integer> flightIdColumn;

    @FXML
    private TableColumn<Booking, String> customerCollumn;


    @FXML
    private TableColumn<Booking, BigDecimal> finalPriceColumn;

    @FXML
    private TableColumn<Booking, LocalDateTime> bookingTimeColumn;

    @FXML
    private TableColumn<Booking, LocalDateTime> flightNumberColumn;

    private CustomerDao customerDao;
    private FlightsDao flightsDao;
    private BookingDao bookingDao;

    @FXML
    public void initialize() throws SQLException {
        bookingDao = new BookingDao();
        flightsDao = new FlightsDao();
        customerDao = new CustomerDao();
        loadBookings();

    }

    @FXML
    public void loadBookings() {
        try {
            List<Booking> bookingsList = bookingDao.getAllBookings();
            ObservableList<Booking> bookings = FXCollections.observableArrayList(bookingsList);
            referenceNumberColumn.setCellValueFactory(new PropertyValueFactory<>("referenceNumber"));
            flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
            customerCollumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            finalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("finalPrice"));
            bookingTimeColumn.setCellValueFactory(new PropertyValueFactory<>("bookingTime"));

            bookingsTable.setItems(bookings);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void navigateToDashboard() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/admin/admin-dashboard.fxml"));
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
    public void navigateToSupport() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/admin/admin-support.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        btnLogout.getParent().getScene().getWindow().hide();
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
}
