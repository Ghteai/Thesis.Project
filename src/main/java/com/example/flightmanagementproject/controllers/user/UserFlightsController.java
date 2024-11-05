package com.example.flightmanagementproject.controllers.user;

import com.example.flightmanagementproject.auth.AuthService;
import com.example.flightmanagementproject.dao.CustomerDao;
import com.example.flightmanagementproject.dao.FlightsDao;
import com.example.flightmanagementproject.models.Customer;
import com.example.flightmanagementproject.models.Flight;
import com.example.flightmanagementproject.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class UserFlightsController {
    @FXML
    private Label name;

    private User loggedInUser;

    @FXML
    private Circle profileCircle;
    private Customer loggedInCustomer;
    private CustomerDao customerDao;
    private FlightsDao flightsDao;
    @FXML
    private TableView<Flight> flightsTable;


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

    @FXML
    public void initialize() throws SQLException {
        flightsDao = new FlightsDao();
        customerDao = new CustomerDao();
        loggedInUser = AuthService.getLoggedInUser();
        System.out.println("logged in user id : "+loggedInUser.getId());
        loggedInCustomer = customerDao.getCustomerByUser(loggedInUser);
        File profilePictureFile = getLoggedInUserProfilePictureFile();
        displayProfilePicture(profilePictureFile);
        name.setText(loggedInUser.getUsername());
        loadFlights();

        flightsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Flight selectedFlight = flightsTable.getSelectionModel().getSelectedItem();

                if (selectedFlight != null) {
                    openBookingWindow(selectedFlight);
                }
            }
        });
    }

    public void loadFlights(){
        List<Flight> flightsList = flightsDao.getAllFlights();

        ObservableList<Flight> flights = FXCollections.observableArrayList(flightsList);
        numberColumn.setCellValueFactory(new PropertyValueFactory<Flight , String>("flightNumber"));
        fromColumn.setCellValueFactory(new PropertyValueFactory<Flight , String>("fromLocation"));
        toColumn.setCellValueFactory(new PropertyValueFactory<Flight , String>("toLocation"));
        departureColumn.setCellValueFactory(new PropertyValueFactory<Flight , LocalDate>("departureDateTime"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<Flight , String>("departureTime"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Flight , Double>("price"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<Flight , Integer>("availableSeats"));

        flightsTable.setItems(flights);
    }

    private void displayProfilePicture(File file) {
        String projectDirectory = System.getProperty("user.dir");
        Image profileImage = new Image(projectDirectory + "/profile-photos/"+loggedInCustomer.getPicture());
        profileCircle.setFill(new ImagePattern(profileImage));
    }

    private File getLoggedInUserProfilePictureFile() {
        User loggedInUser = getLoggedInUser();
        return new File("path/to/profile/pictures/" + loggedInUser.getUsername() + "-photo");
    }

    public void openBookingWindow(Flight selectedFlight){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/flightmanagementproject/user/user-book-flight.fxml"));
            Parent root = loader.load();

            UserBookFlightController controller = loader.getController();

            controller.setFlight(selectedFlight);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Book Flight");
            stage.setScene(new Scene(root));

            stage.initOwner(flightsTable.getScene().getWindow());

            stage.showAndWait();
            loadFlights();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private User getLoggedInUser() {
        return AuthService.getLoggedInUser();
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

    @FXML
    public void navigateToDashboard() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/user/user-dashboard.fxml"));
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
