package com.example.flightmanagementproject.controllers.admin;

import com.example.flightmanagementproject.dao.BookingDao;
import com.example.flightmanagementproject.dao.ComplaintDao;
import com.example.flightmanagementproject.dao.CustomerDao;
import com.example.flightmanagementproject.dao.FlightsDao;
import com.example.flightmanagementproject.models.Booking;
import com.example.flightmanagementproject.models.Complaint;
import com.example.flightmanagementproject.models.Flight;
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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminComplaintsController {
    @FXML
    private Button btnLogout;

    private ComplaintDao complaintDao;
    @FXML
    private TableView<Complaint> complaintsTable;
    @FXML
    private TableColumn<Complaint, String> titleCollumn;
    @FXML
    private TableColumn<Complaint, String> TypeCollumn;
    @FXML
    private TableColumn<Complaint, String> CustomerCollumn;

    @FXML
    public void initialize() throws SQLException {
        complaintDao = new ComplaintDao();
        loadComplaints();

        complaintsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Complaint selectedComplaint = complaintsTable.getSelectionModel().getSelectedItem();

                if (selectedComplaint != null) {
                    try {
                        openComplaintsDetails(selectedComplaint);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });


    }

    private void openComplaintsDetails(Complaint complaint) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/flightmanagementproject/admin/complaint-details.fxml"));
        Parent root = loader.load();

        ComplaintsDetails complaintsDetailsController = loader.getController();

        complaintsDetailsController.initialize(
                complaint.getTitle(),
                complaint.getType(),
                complaint.getCustomerName(),
                complaint.getDescription()
        );

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

    }

    @FXML
    public void loadComplaints() {
        List<Complaint> complaintList = complaintDao.getAllComplaints();
        ObservableList<Complaint> complaints = FXCollections.observableArrayList(complaintList);
        titleCollumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        TypeCollumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        CustomerCollumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        complaintsTable.setItems(complaints);
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
    public void navigateToBookings() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/admin/admin-bookings.fxml"));
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
