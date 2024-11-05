package com.example.flightmanagementproject.controllers.admin;

import com.example.flightmanagementproject.dao.FlightsDao;
import com.example.flightmanagementproject.models.Flight;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class FlightDetailsController {

    @FXML
    private Label flightId;

    @FXML
    private TextField flightNumberField;

    @FXML
    private TextField fromField;

    @FXML
    private TextField toField;

    @FXML
    private DatePicker departureField;

    @FXML
    private TextField timeField;


    @FXML
    private TextField priceField;

    @FXML
    private TextField seatsField;

    private int id;

    public void setFlight(Flight flight) {
        id = flight.getFlightID();
        flightId.setText("Flight Id : "+flight.getFlightID());
        flightNumberField.setText(flight.getFlightNumber());
        fromField.setText(flight.getFromLocation());
        toField.setText(flight.getToLocation());
        departureField.setValue(flight.getDepartureDateTime());
        timeField.setText(flight.getDepartureTime());
        priceField.setText(String.valueOf(flight.getPrice()));
        seatsField.setText(String.valueOf(flight.getAvailableSeats()));
    }


    @FXML
    private void saveFlight() {
        int flightId = id;
        String flightNumber = flightNumberField.getText();
        String fromLocation = fromField.getText();
        String toLocation = toField.getText();
        LocalDate departureDate = departureField.getValue();
        String departureTime = timeField.getText();
        double price = Double.parseDouble(priceField.getText());
        int seats = Integer.parseInt(seatsField.getText());

        Flight updatedFlight = new Flight();
        updatedFlight.setFlightID(flightId);
        updatedFlight.setFlightNumber(flightNumber);
        updatedFlight.setFromLocation(fromLocation);
        updatedFlight.setToLocation(toLocation);
        updatedFlight.setDepartureDateTime(departureDate);
        updatedFlight.setDepartureTime(departureTime);
        updatedFlight.setPrice(price);
        updatedFlight.setAvailableSeats(seats);

        FlightsDao flightsDao = new FlightsDao();
        flightsDao.updateFlight(updatedFlight);

        closeWindow();
    }

    @FXML
    private void deleteFlight() {
        // Retrieve flight ID
        int flightId = id; // Assuming flightId is an integer

        // Delete the flight from the database
        FlightsDao flightsDao = new FlightsDao();
        flightsDao.deleteFlight(flightId);

        // Close the window
        closeWindow();
    }

    private void closeWindow() {
        // Get a handle to the stage
        Stage stage = (Stage) flightId.getScene().getWindow();
        // Close the window
        stage.close();
    }
}
