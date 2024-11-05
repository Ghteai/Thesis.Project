package com.example.flightmanagementproject.controllers.admin;

import com.example.flightmanagementproject.dao.CustomerDao;
import com.example.flightmanagementproject.models.Customer;
import com.example.flightmanagementproject.models.Flight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class CustomersAdmin {
    @FXML
    private Button btnLogout;

    @FXML
    private TableView<Customer> CustomersTable;

    @FXML
    private TextField search;

    @FXML
    private TableColumn<Customer , Integer> idColumn;

    @FXML
    private TableColumn<Customer , String> nameCollumn;

    @FXML
    private TableColumn<Customer , String> nationalityCollumn;

    @FXML
    private TableColumn<Customer , String> passportCollumn;





    private CustomerDao customerDao;



    @FXML
    private void initialize() {
        System.out.println("Initialize method called");
        customerDao = new CustomerDao();
        loadCustomers();

        CustomersTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Customer selectedCustomer = CustomersTable.getSelectionModel().getSelectedItem();

                if (selectedCustomer != null) {
                    openCustomerDetailsPopup(selectedCustomer);
                }
            }
        });
    }

    @FXML
    public void logout() throws IOException {
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
    public void navigateToSupport() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/admin/admin-support.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        btnLogout.getParent().getScene().getWindow().hide();
    }


    private void loadCustomers() {
        System.out.println("Loading Customers...");
        List<Customer> CustomersList = customerDao.getAllCustomers();



        ObservableList<Customer> Customers = FXCollections.observableArrayList(CustomersList);
        idColumn.setCellValueFactory(new PropertyValueFactory<Customer , Integer>("customerID"));
        nameCollumn.setCellValueFactory(new PropertyValueFactory<Customer , String>("name"));
        nationalityCollumn.setCellValueFactory(new PropertyValueFactory<Customer , String>("nationality"));

        passportCollumn.setCellValueFactory(new PropertyValueFactory<Customer , String>("passportNumber"));


        CustomersTable.setItems(Customers);
    }



    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void openCustomerDetailsPopup(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/flightmanagementproject/admin/customer-details.fxml"));
            Parent root = loader.load();

            CustomerDetailsController controller = loader.getController();

            controller.setCustomer(customer);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Customer Details");
            stage.setScene(new Scene(root));

            stage.initOwner(CustomersTable.getScene().getWindow());

            // Show the popup
            stage.showAndWait();
            loadCustomers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void navigateToDashboard() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/admin/admin-dashboard.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        btnLogout.getParent().getScene().getWindow().hide();
    }

    @FXML
    public void navigateToFlights() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/admin/flights-admin.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        btnLogout.getParent().getScene().getWindow().hide();
    }

    @FXML
    public void searchCustomer(){
        System.out.println("button clicked");
        String passportNumber = search.getText().trim();
        if(!passportNumber.isEmpty()){
            Customer foundCustomer = customerDao.getCustomerByNumber(passportNumber);
            if(foundCustomer != null){
                openCustomerDetailsPopup(foundCustomer);
            }
            else
                showErrorDialog("Customer Not Found" , "Customer With Passeport Number : "+passportNumber+" Not Found");
        }
        else
            showErrorDialog("Empty Search Field", "Please enter a customer number to search.");

    }


    @FXML
    public void navigateToCustomers() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/admin/customers-admin.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        btnLogout.getParent().getScene().getWindow().hide();
    }
    @FXML
    public void navigateToTickets() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/flightmanagementproject/admin/admin-bookings.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        btnLogout.getParent().getScene().getWindow().hide();
    }

}
