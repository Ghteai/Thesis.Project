package com.example.flightmanagementproject.auth;

import com.example.flightmanagementproject.dao.UserDao;
import com.example.flightmanagementproject.models.Customer;
import com.example.flightmanagementproject.models.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class RegisterController {
    @FXML
    private JFXButton upload;

    @FXML
    private TextField nameField;
    @FXML
    private TextField nationalityField;
    @FXML
    private TextField passeportField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private JFXRadioButton maleRadio;
    @FXML
    private JFXRadioButton femailRadio;
    @FXML
    private DatePicker dobField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private DatePicker passportExpField;

    @FXML
    private ImageView logo;
    @FXML
    private ImageView profilePic;

    private UserDao userDao;

    private String profilePhotoName;
    @FXML
    private ToggleGroup genderToggleGroup;

    @FXML
    private void initialize() throws URISyntaxException {
        genderToggleGroup = new ToggleGroup();
        loadDefaultImages();
        maleRadio.setToggleGroup(genderToggleGroup);
        femailRadio.setToggleGroup(genderToggleGroup);
        userDao = new UserDao();
    }

    private void loadDefaultImages() throws URISyntaxException {
        Image profileImage = new Image(getClass().getResource("/com/example/flightmanagementproject/images/profile_photo.png").toURI().toString());
        profilePic.setImage(profileImage);

        Image logoImage = new Image(getClass().getResource("/com/example/flightmanagementproject/images/logo_plane.png").toURI().toString());
        logo.setImage(logoImage);
    }

    @FXML
    private void handleUploadPicture(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        // Show the file dialog
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            profilePhotoName = generateRandomString(5) + ".jpeg";
            String filename = profilePhotoName;

            String projectDirectory = System.getProperty("user.dir");

            // Set the destination folder path within the project directory
            File destinationFolder = new File(projectDirectory + "/profile-photos");
            File destinationFile = new File(destinationFolder, filename);

            try {
                // Create the folder if it doesn't exist
                if (!destinationFolder.exists()) {
                    destinationFolder.mkdirs();
                }

                // Copy the selected file to the destination folder
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Display the image
                displayImage(destinationFile);
            } catch (IOException e) {
                e.printStackTrace();
                showErrorAlert("There was an error uploading the image");
            }
        }
    }

    private String generateRandomString(int length) {
        return UUID.randomUUID().toString().substring(0, length);
    }


    private void displayImage(File file) {
        try {
            Image image = new Image(file.toURI().toURL().toString());
            profilePic.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("There was an error displaying the image");
        }
    }


    @FXML
    public void register() {
        // Validate fields
        if (nameField.getText().isEmpty() || nationalityField.getText().isEmpty() ||
                passeportField.getText().isEmpty() || usernameField.getText().isEmpty() ||
                passwordField.getText().isEmpty() || genderToggleGroup.getSelectedToggle() == null ||
                dobField.getValue() == null || emailField.getText().isEmpty() ||
                phoneField.getText().isEmpty() || passportExpField.getValue() == null ||
                profilePhotoName == null) {
            showErrorAlert("Please fill in all fields and upload a profile picture.");
            return; // Stop the registration process if validation fails
        }

        // Continue with registration
        String name = nameField.getText();
        String nationality = nationalityField.getText();
        String passport = passeportField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String gender = maleRadio.isSelected() ? "Male" : "Female";
        LocalDate dateOfBirth = dobField.getValue();
        String email = emailField.getText();
        String phone = phoneField.getText();
        LocalDate passportExpiryDate = passportExpField.getValue();
        String filename = profilePhotoName;

        User user = new User(username, password, false);
        Customer customer = new Customer(name, nationality, passport, filename, gender, dateOfBirth, phone, email, passportExpiryDate);

        try {
            userDao.registerUser(user, customer);
            showSuccessAlert();
            clearFields();
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace();
            showErrorAlert("There was an error trying to register!");
        }
    }


    private void showSuccessAlert() {
        showAlert("Success", "Successfully registered! You can now login", Alert.AlertType.INFORMATION);
    }

    private void showErrorAlert(String text) {
        showAlert("ERROR", text, Alert.AlertType.ERROR);
    }

    private void showAlert(String title, String text, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public void clearFields() throws URISyntaxException {
        loadDefaultImages();
        nameField.clear();
        nationalityField.clear();
        passeportField.clear();
        usernameField.clear();
        passwordField.clear();
        emailField.clear();
        phoneField.clear();
        dobField.setValue(null);
        maleRadio.setSelected(false);
        femailRadio.setSelected(false);
        passportExpField.setValue(null);
    }

    @FXML
    public void close(){
        Platform.exit();
    }

    @FXML
    public void goToSignIn() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        nameField.getParent().getScene().getWindow().hide();
    }
}
