package com.example.flightmanagementproject.controllers.admin;

import com.example.flightmanagementproject.dao.CustomerDao;
import com.example.flightmanagementproject.dao.FlightsDao;
import com.example.flightmanagementproject.models.Customer;
import com.example.flightmanagementproject.models.Flight;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.time.LocalDate;

public class CustomerDetailsController {
    @FXML
    private Label idField;
    @FXML
    private Circle profileCircle;
    @FXML
    private TextField nameField;
    @FXML
    private TextField passportField;
    @FXML
    private TextField nationalityField;

    @FXML
    private JFXComboBox<String> genderField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private DatePicker dobField;

    @FXML
    private DatePicker passportValidField;


    int id ;


    public void setCustomer(Customer customer) {
        genderField.getItems().addAll("Male" , "Female");
        id = customer.getCustomerID();
        idField.setText("Customer id: " + customer.getCustomerID());
        String projectDirectory = System.getProperty("user.dir");
        Image profileImage = new Image(projectDirectory + "/profile-photos/" + customer.getPicture());
        profileCircle.setFill(new ImagePattern(profileImage));
        nameField.setText(customer.getName());
        passportField.setText(customer.getPassportNumber());
        nationalityField.setText(customer.getNationality());
        if(customer.getGender().equals("Male"))
            genderField.setValue("Male");
        else
            genderField.setValue("Female");
        phoneField.setText(customer.getPhone());
        emailField.setText(customer.getEmail());
        dobField.setValue(customer.getDateOfBirth());
        passportValidField.setValue(customer.getPassportExpiryDate());
    }

    public void saveCustomer(ActionEvent actionEvent) {
        int customerId = id;
        String customerName = nameField.getText();
        String passportNumber = passportField.getText();
        String nationality = nationalityField.getText();
        String gender = genderField.getValue(); // Assuming String for gender, adjust accordingly
        String phoneNumber = phoneField.getText();
        String email = emailField.getText();
        LocalDate dob = dobField.getValue();
        LocalDate passportValidUntil = passportValidField.getValue();

        Customer updatedCustomer = new Customer();
        updatedCustomer.setCustomerID(customerId);
        updatedCustomer.setName(customerName);
        updatedCustomer.setPassportNumber(passportNumber);
        updatedCustomer.setNationality(nationality);
        updatedCustomer.setGender(gender);
        updatedCustomer.setPhone(phoneNumber);
        updatedCustomer.setEmail(email);
        updatedCustomer.setDateOfBirth(dob);
        updatedCustomer.setPassportExpiryDate(passportValidUntil);

        CustomerDao customerDao = new CustomerDao();
        customerDao.updateCustomer(updatedCustomer);

        closeWindow();
    }


    public void deleteCustomer(ActionEvent actionEvent) {
        int customerId = id;

        CustomerDao customerDao = new CustomerDao();
        customerDao.deleteCustomer(customerId);

        closeWindow();
    }

    private void closeWindow() {
        // Get a handle to the stage
        Stage stage = (Stage) idField.getScene().getWindow();
        // Close the window
        stage.close();
    }
}
