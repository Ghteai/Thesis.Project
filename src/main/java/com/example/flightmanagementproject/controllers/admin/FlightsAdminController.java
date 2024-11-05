package com.example.flightmanagementproject.controllers.admin;

import com.example.flightmanagementproject.dao.FlightsDao;
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
import java.time.LocalDate;
import java.util.List;

public class FlightsAdminController {

    private FlightsDao flightsDao;


    @FXML
    private Button btnLogout;

    @FXML
    private TableView<Flight> flightsTable;

    @FXML
    private TableColumn<Flight, Integer> idColumn;

    @FXML
    private TableColumn<Flight, String> numberColumn;

    @FXML
    private TableColumn<Flight, String> fromColumn;

    @FXML
    private TableColumn<Flight, String> toColumn;

    @FXML
    private TableColumn<Flight, LocalDate> departureColumn;

    @FXML
    private TableColumn<Flight, String> timeColumn;


    @FXML
    private TableColumn<Flight, Double> priceColumn;

    @FXML
    private TableColumn<Flight, Integer> seatsColumn;

    @FXML TextField search;

    @FXML
    private Button dashboard;
    @FXML
    private Button customers;
    @FXML
    private Button tickets;


    @FXML
    private void initialize() {
        System.out.println("Initialize method called");
        flightsDao = new FlightsDao();
        loadFlights();

        flightsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                // Get the selected flight from the table
                Flight selectedFlight = flightsTable.getSelectionModel().getSelectedItem();

                if (selectedFlight != null) {
                    // Open the popup with the selected flight information
                    openFlightDetailsPopup(selectedFlight);
                }
            }
        });
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
    private void handleAddFlight() {
        // Load the Add Flight popup
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/flightmanagementproject/admin/add-flight.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add a New Flight");
            stage.setScene(new Scene(root));

            // Set the owner of the stage
            stage.initOwner(flightsTable.getScene().getWindow());

            // Show and wait for the popup to close
            stage.showAndWait();

            // After the popup is closed, refresh the TableView
            loadFlights();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., display an error message)
        }
    }

    private void loadFlights() {
        System.out.println("Loading flights...");
        List<Flight> flightsList = flightsDao.getAllFlights();



        ObservableList<Flight> flights = FXCollections.observableArrayList(flightsList);
        idColumn.setCellValueFactory(new PropertyValueFactory<Flight , Integer>("flightID"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<Flight , String>("flightNumber"));
        fromColumn.setCellValueFactory(new PropertyValueFactory<Flight , String>("fromLocation"));
        toColumn.setCellValueFactory(new PropertyValueFactory<Flight , String>("toLocation"));
        departureColumn.setCellValueFactory(new PropertyValueFactory<Flight , LocalDate>("departureDateTime"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<Flight , String>("departureTime"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Flight , Double>("price"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<Flight , Integer>("availableSeats"));

        flightsTable.setItems(flights);
    }

    @FXML
    public void searchFlight(){
        System.out.println("button clicked");
        String flightNumber = search.getText().trim();
        if(!flightNumber.isEmpty()){
            Flight foundFlight = flightsDao.getFlightByNumber(flightNumber);
            if(foundFlight != null){
                openFlightDetailsPopup(foundFlight);
            }
            else
                showErrorDialog("Flight Not Found" , "Flight With Number : "+flightNumber+" Not Found");
        }
        else
            showErrorDialog("Empty Search Field", "Please enter a flight number to search.");

    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void openFlightDetailsPopup(Flight flight) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/flightmanagementproject/admin/edit-flight.xml.fxml"));
            Parent root = loader.load();

            FlightDetailsController controller = loader.getController();

            controller.setFlight(flight);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Flight Details");
            stage.setScene(new Scene(root));

            stage.initOwner(flightsTable.getScene().getWindow());

            // Show the popup
            stage.showAndWait();
            loadFlights();
        } catch (IOException e) {
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
    public void navigateToTickets() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/admin/admin-bookings.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        btnLogout.getParent().getScene().getWindow().hide();
    }


}
