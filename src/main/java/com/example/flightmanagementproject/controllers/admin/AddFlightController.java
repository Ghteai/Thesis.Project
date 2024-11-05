package com.example.flightmanagementproject.controllers.admin;

import com.example.flightmanagementproject.dao.FlightsDao;
import com.example.flightmanagementproject.models.Flight;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddFlightController {

    @FXML
    private TextField flightNumberField;

    @FXML
    private TextField departureField;

    @FXML
    private TextField arrivalField;

    @FXML
    private DatePicker departureDateField;

    @FXML
    private TextField departureTimeField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField seatsField;

    @FXML
    private void addFlight() {
        // Get data from fields
        String flightNumber = flightNumberField.getText();
        String departure = departureField.getText();
        String arrival = arrivalField.getText();
        LocalDate  departureDate = departureDateField.getValue();
        String departureTime = departureTimeField.getText();
        double price = Double.parseDouble(priceField.getText());
        int seats = Integer.parseInt(seatsField.getText());


        // Create a new Flight object
        Flight newFlight = new Flight();
        newFlight.setFlightNumber(flightNumber);
        newFlight.setFromLocation(departure);
        newFlight.setToLocation(arrival);
        newFlight.setDepartureDateTime(departureDate);
        newFlight.setDepartureTime(departureTime);
        newFlight.setPrice(price);
        newFlight.setAvailableSeats(seats);

        // Add the new flight to the database
        FlightsDao flightsDao = new FlightsDao();
        flightsDao.addFlight(newFlight);

        Stage stage = (Stage) flightNumberField.getScene().getWindow();
        stage.close();
    }
}

