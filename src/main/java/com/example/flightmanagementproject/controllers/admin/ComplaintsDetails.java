package com.example.flightmanagementproject.controllers.admin;

import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ComplaintsDetails {
    @FXML
    private Label title;

    @FXML
    private Label type;

    @FXML
    private Label customer;

    @FXML
    private JFXTextArea description;
    public void initialize(String titleText, String typeText, String customerText, String descriptionText) {
        title.setText(titleText);
        type.setText(typeText);
        customer.setText(customerText);
        description.setText(descriptionText);
    }
}
