package com.example.flightmanagementproject.models;

import java.time.LocalDate;
import java.util.Objects;

public class Customer {

    private int customerID;
    private String name;
    private String nationality;
    private String passportNumber;
    private String picture;
    private String gender;
    private LocalDate dateOfBirth;
    private String phone;
    private String email;
    private LocalDate passportExpiryDate;


    public Customer(String name, String nationality, String passportNumber, String picture,
                    String gender, LocalDate dateOfBirth, String phone, String email, LocalDate passportExpiryDate) {
        this.name = name;
        this.nationality = nationality;
        this.passportNumber = passportNumber;
        this.picture = picture;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.email = email;
        this.passportExpiryDate = passportExpiryDate;
    }

    public Customer(int customerID, String name, String nationality, String passportNumber, String picture,
                    String gender, LocalDate dateOfBirth, String phone, String email, LocalDate passportExpiryDate) {
        this.customerID = customerID;
        this.name = name;
        this.nationality = nationality;
        this.passportNumber = passportNumber;
        this.picture = picture;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.email = email;
        this.passportExpiryDate = passportExpiryDate;
    }

    public Customer() {
        // Default constructor
    }

    public Customer(int id , String name, String nationality, String passportNumber, String picture) {
        this.customerID = id;
        this.name = name;
        this.nationality = nationality;
        this.passportNumber = passportNumber;
        this.picture = picture;
    }

    public Customer(String name, String nationality, String passportNumber, String picture) {
        this.name = name;
        this.nationality = nationality;
        this.passportNumber = passportNumber;
        this.picture = picture;
    }

    // Getters and Setters

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getPassportExpiryDate() {
        return passportExpiryDate;
    }

    public void setPassportExpiryDate(LocalDate passportExpiryDate) {
        this.passportExpiryDate = passportExpiryDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerID, name, nationality, passportNumber, picture);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerID == customer.customerID &&
                Objects.equals(name, customer.name) &&
                Objects.equals(nationality, customer.nationality) &&
                Objects.equals(passportNumber, customer.passportNumber) &&
                Objects.equals(picture, customer.picture);
    }


    @Override
    public String toString() {
        return "Customer{" +
                "customerID=" + customerID +
                ", name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                ", picture='" + picture + '\'' +
                '}';

    }
}