package com.example.flightmanagementproject.models;

import java.time.LocalDate;
import java.time.LocalDate;

public class Flight {
    private int flightID;
    private String flightNumber;
    private  String fromLocation;
    private  String toLocation;
    private LocalDate departureDateTime;
    private String departureTime;
    private  double price;
    private  int availableSeats;

    public Flight(){};

    public Flight(int flightID, String flightNumber, String fromLocation, String toLocation,
                  LocalDate departureDateTime, String departureTime , double price, int availableSeats) {
        this.flightID = flightID;
        this.flightNumber = flightNumber;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.departureDateTime = departureDateTime;
        this.departureTime = departureTime;
        this.price = price;
        this.availableSeats = availableSeats;
    }

    public int getFlightID() {
        return flightID;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public LocalDate getDepartureDateTime() {
        return departureDateTime;
    }

    public double getPrice() {
        return price;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public void setDepartureDateTime(LocalDate departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
}
