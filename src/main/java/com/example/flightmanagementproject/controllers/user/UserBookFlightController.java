package com.example.flightmanagementproject.controllers.user;

import com.example.flightmanagementproject.auth.AuthService;
import com.example.flightmanagementproject.dao.BookingDao;
import com.example.flightmanagementproject.dao.CustomerDao;
import com.example.flightmanagementproject.dao.FlightsDao;
import com.example.flightmanagementproject.models.Booking;
import com.example.flightmanagementproject.models.Customer;
import com.example.flightmanagementproject.models.Flight;
import com.example.flightmanagementproject.models.User;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;

public class UserBookFlightController {
    private Flight selectedFlight;

    @FXML
    private Label flightIdLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label flightDetailsLabel;

    @FXML
    private Label flightNumberLabel;

    @FXML
    private Label departureTimeLabel;

    @FXML
    private Label fromLabel;

    @FXML
    private Label passengerDetailsLabel;

    @FXML
    private Label fullNameLabel;

    @FXML
    private Label passportNumberLabel;

    @FXML
    private Label nationalityLabel;

    @FXML
    private Label dobLabel;
    @FXML
    private Label genderLabel;
    @FXML
    private Label passportExpiryLabel;

    @FXML
    private Label toLabel;

    @FXML
    private JFXRadioButton economyRadioButton;

    @FXML
    private JFXRadioButton businessClassRadioButton;

    @FXML
    private ToggleGroup classToggleGroup;

    @FXML
    private JFXCheckBox extraLegRoomCheckBox;

    @FXML
    private Label priceLabel;

    private User loggedInUser;
    private Customer customer;
    private CustomerDao customerDao;

    private double totalPrice;

    @FXML
    private Label classPrice;
    @FXML
    private Label extraLegRoomPrice;
    private BookingDao bookingDao;
    private FlightsDao flightsDao;

    @FXML
    public void initialize() throws SQLException {
        customerDao = new CustomerDao();
        flightsDao = new FlightsDao();
        loggedInUser = AuthService.getLoggedInUser();
        System.out.println("logged in user : "+loggedInUser.getId());
        customer = customerDao.getCustomerByUser(loggedInUser);

        economyRadioButton.setToggleGroup(classToggleGroup);
        businessClassRadioButton.setToggleGroup(classToggleGroup);

        classToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == businessClassRadioButton) {
                classPrice.setText("+"+selectedFlight.getPrice()*0.3+"$");

            } else {
                classPrice.setText("");
            }
        });

        extraLegRoomCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                extraLegRoomPrice.setText("+20$");
            } else {
                extraLegRoomPrice.setText("");
            }
        });
    }

    public void setFlight(Flight flight) {
        this.selectedFlight = flight;
        populateFields();
    }

    private void populateFields() {
        flightIdLabel.setText("Flight id :"+selectedFlight.getFlightID());
        flightNumberLabel.setText("Flight Number : "+selectedFlight.getFlightNumber());
        departureTimeLabel.setText("Departure at : "+selectedFlight.getDepartureTime());
        fromLabel.setText("From : "+selectedFlight.getFromLocation());
        toLabel.setText("To : "+selectedFlight.getToLocation());
        priceLabel.setText("Price : "+selectedFlight.getPrice()+"$");

        fullNameLabel.setText("Full Name : "+customer.getName());
        passportNumberLabel.setText("Passport Number : "+customer.getPassportNumber());
        nationalityLabel.setText("Nationality : "+customer.getNationality());
        dobLabel.setText("Date Of Birth : "+customer.getDateOfBirth());
        genderLabel.setText("Gender : "+customer.getGender());
        passportExpiryLabel.setText("Passport Expiry : "+customer.getPassportExpiryDate());
    }

    @FXML
    private void bookFlight() {
        if (selectedFlight == null) {
            // Handle the case where no flight is selected
            return;
        }

        String classType;
        if (businessClassRadioButton.isSelected()) {
            classType = "Business";
        } else {
            classType = "Economy";
        }

        boolean hasExtraLegRoom = extraLegRoomCheckBox.isSelected();
        double classPrice = businessClassRadioButton.isSelected() ? selectedFlight.getPrice() * 0.3 : 0;
        double extraLegRoomPrice = hasExtraLegRoom ? 20 : 0;

        totalPrice = selectedFlight.getPrice() + classPrice + extraLegRoomPrice;

        Booking booking = new Booking(
                generateReferenceNumber(),
                customer.getCustomerID(),
                selectedFlight.getFlightID(),
                classType,
                hasExtraLegRoom,
                BigDecimal.valueOf(totalPrice),
                LocalDateTime.now()
        );

        bookingDao = new BookingDao();
        bookingDao.saveBooking(booking);
        closeWindow();

        showAlert("Booking Success! " , "You have succeffuly made a booking , you can print your E-Ticket now or you can find your booking in the : My Booking tab!" , Alert.AlertType.INFORMATION);

        selectedFlight.setAvailableSeats(selectedFlight.getAvailableSeats()-1);
        flightsDao.updateFlight(selectedFlight);

    }

    private void closeWindow() {
        Stage stage = (Stage) priceLabel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String text, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    private String generateReferenceNumber() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder referenceNumber = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int randomIndex = new Random().nextInt(letters.length());
            referenceNumber.append(letters.charAt(randomIndex));
        }

        for (int i = 0; i < 4; i++) {
            referenceNumber.append(new Random().nextInt(10));
        }

        return referenceNumber.toString();
    }
}
