package com.example.flightmanagementproject.controllers.user;

import com.example.flightmanagementproject.auth.AuthService;
import com.example.flightmanagementproject.dao.BookingDao;
import com.example.flightmanagementproject.dao.CustomerDao;
import com.example.flightmanagementproject.dao.FlightsDao;
import com.example.flightmanagementproject.models.Booking;
import com.example.flightmanagementproject.models.Customer;
import com.example.flightmanagementproject.models.User;
import com.jfoenix.controls.JFXButton;
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
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class UserBookingController {
    @FXML
    private JFXButton btnLogout;

    @FXML
    private Label name;

    private User loggedInUser;

    @FXML
    private Circle profileCircle;
    private Customer loggedInCustomer;
    private CustomerDao customerDao;
    private FlightsDao flightsDao;
    private BookingDao bookingDao;

    @FXML
    private TableView<Booking> bookingsTable;

    @FXML
    private TableColumn<Booking, String> referenceNumberColumn;

    @FXML
    private TableColumn<Booking, Integer> flightIdColumn;

    @FXML
    private TableColumn<Booking, String> classTypeColumn;

    @FXML
    private TableColumn<Booking, Boolean> extraLegRoomColumn;

    @FXML
    private TableColumn<Booking, BigDecimal> finalPriceColumn;

    @FXML
    private TableColumn<Booking, LocalDateTime> bookingTimeColumn;

    @FXML
    private TableColumn<Booking, LocalDateTime> flightNumberColumn;


    @FXML
    public void initialize() throws SQLException {
        bookingDao = new BookingDao();
        flightsDao = new FlightsDao();
        customerDao = new CustomerDao();
        loggedInUser = AuthService.getLoggedInUser();
        System.out.println("logged in user id : "+loggedInUser.getId());
        loggedInCustomer = customerDao.getCustomerByUser(loggedInUser);
        File profilePictureFile = getLoggedInUserProfilePictureFile();
        displayProfilePicture(profilePictureFile);
        name.setText(loggedInUser.getUsername());
        loadBookings();
        bookingsTable.setOnMouseClicked(event -> {
                Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
                if (selectedBooking != null) {
                    openBookingPrintView(selectedBooking);
                }
        });
    }

    @FXML
    public void loadBookings() {
        try {
            List<Booking> bookingsList = bookingDao.getAllBookingsForCustomer(loggedInCustomer.getCustomerID());
            ObservableList<Booking> bookings = FXCollections.observableArrayList(bookingsList);
            referenceNumberColumn.setCellValueFactory(new PropertyValueFactory<>("referenceNumber"));
            classTypeColumn.setCellValueFactory(new PropertyValueFactory<>("classType"));
            extraLegRoomColumn.setCellValueFactory(new PropertyValueFactory<>("extraLegRoom"));
            flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));

            finalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("finalPrice"));
            bookingTimeColumn.setCellValueFactory(new PropertyValueFactory<>("bookingTime"));

            bookingsTable.setItems(bookings);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            btnLogout.getParent().getScene().getWindow().hide();
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
        btnLogout.getParent().getScene().getWindow().hide();
    }

    @FXML
    public void navigateToDashboard(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/user/user-dashboard.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        btnLogout.getParent().getScene().getWindow().hide();
    }

    private void displayProfilePicture(File file) {
        String projectDirectory = System.getProperty("user.dir");
        Image profileImage = new Image(projectDirectory + "/profile-photos/"+loggedInCustomer.getPicture());
        profileCircle.setFill(new ImagePattern(profileImage));
    }

    private File getLoggedInUserProfilePictureFile() {
        User loggedInUser = AuthService.getLoggedInUser();
        return new File("path/to/profile/pictures/" + loggedInUser.getUsername() + "-photo");
    }


    private void openBookingPrintView(Booking booking) {
        try {
            Booking completeBooking = bookingDao.getBookingWithDetails(booking.getReferenceNumber());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/flightmanagementproject/user/booking-details.fxml"));
            Parent root = loader.load();

            BookingPrintController controller = loader.getController();
            controller.setBooking(booking);


            controller.initializeData();

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
            loadBookings();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
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
