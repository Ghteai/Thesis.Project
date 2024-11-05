package com.example.flightmanagementproject.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Booking {
    private int id;
    private String referenceNumber;
    private int customerId;
    private int flightId;
    private String classType;
    private boolean extraLegRoom;
    private BigDecimal finalPrice;
    private LocalDateTime bookingTime;

    private String flightNumber;

    private Flight flight;
    private Customer customer;
    private String customerName;


    // Constructors, getters, and setters
    public Booking(){

    }

    public Booking(int id , String referenceNumber, int customerId, int flightId, String classType, boolean extraLegRoom, BigDecimal finalPrice, LocalDateTime bookingTime) {
        this.id = id;
        this.referenceNumber = referenceNumber;
        this.customerId = customerId;
        this.flightId = flightId;
        this.classType = classType;
        this.extraLegRoom = extraLegRoom;
        this.finalPrice = finalPrice;
        this.bookingTime = bookingTime;
    }

    public Booking(int id ,String referenceNumber, int customerId, int flightId, String flightNumber, String classType,
                   boolean extraLegRoom, BigDecimal finalPrice, LocalDateTime bookingTime) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.referenceNumber = referenceNumber;
        this.customerId = customerId;
        this.flightId = flightId;
        this.classType = classType;
        this.extraLegRoom = extraLegRoom;
        this.finalPrice = finalPrice;
        this.bookingTime = bookingTime;
    }


    public Booking(String referenceNumber, int customerId, int flightId, String classType, boolean extraLegRoom, BigDecimal finalPrice, LocalDateTime bookingTime) {
        this.referenceNumber = referenceNumber;
        this.customerId = customerId;
        this.flightId = flightId;
        this.classType = classType;
        this.extraLegRoom = extraLegRoom;
        this.finalPrice = finalPrice;
        this.bookingTime = bookingTime;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getClassType() {
        return classType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public boolean isExtraLegRoom() {
        return extraLegRoom;
    }

    public void setExtraLegRoom(boolean extraLegRoom) {
        this.extraLegRoom = extraLegRoom;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
