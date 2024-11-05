package com.example.flightmanagementproject.dao;

import com.example.flightmanagementproject.models.Customer;
import com.example.flightmanagementproject.models.Flight;
import com.example.flightmanagementproject.models.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {
    private DatabaseConnection databaseConnection;
    public CustomerDao() {

        databaseConnection = new DatabaseConnection();
    }
    public Customer getCustomerByUser(User user) throws SQLException {
        Connection connection = databaseConnection.getConnection();
        String query = "SELECT * FROM customers WHERE userId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, user.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return createCustomerFromResultSet(resultSet);
                }
            }
        }

        return null;
    }

    private Customer createCustomerFromResultSet(ResultSet resultSet) throws SQLException {
        int customerID = resultSet.getInt("CustomerID");
        String name = resultSet.getString("name");
        String nationality = resultSet.getString("nationality");
        String passportNumber = resultSet.getString("passportNumber");
        String picture = resultSet.getString("picture");
        String gender = resultSet.getString("gender");
        LocalDate dateOfBirth = resultSet.getDate("dateOfBirth").toLocalDate();
        String phone = resultSet.getString("phone");
        String email = resultSet.getString("email");
        LocalDate passportExpiryDate = resultSet.getDate("passport_expiry_date").toLocalDate();

        return new Customer(customerID, name, nationality, passportNumber, picture, gender, dateOfBirth, phone, email, passportExpiryDate);
    }


    public List<Customer> getAllCustomers() {
        Connection connection = databaseConnection.getConnection();
        List<Customer> customers = new ArrayList<>();

        String query = "SELECT * FROM Customers";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                customers.add(createCustomerFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    public Customer getCustomerByNumber(String customerNumber) {
        Connection connection = databaseConnection.getConnection();

        String query = "SELECT * FROM Customers WHERE PassportNumber = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, customerNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return createCustomerFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Customer getCustomerById(int customerId) throws SQLException {
        Customer customer = null;

        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        String sql = "SELECT * FROM Customers WHERE customerId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, customerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return createCustomerFromResultSet(resultSet);
                }
            }
        }

        return customer;
    }



    public void deleteCustomer(int customerId) {
        String sql = "DELETE FROM customers WHERE customerId = ?";
        Connection connection = databaseConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // Handle the exception appropriately (log or throw a custom exception)
        }
    }

    public void updateCustomer(Customer customer) {
        String sql = "UPDATE Customers SET Name = ?, Nationality = ?, PassportNumber = ?, Gender = ?, DateOfBirth = ?, Phone = ?, Email = ?, Passport_expiry_date = ?" +
                "WHERE CustomerID = ?";
        Connection connection = databaseConnection.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getNationality());
            statement.setString(3, customer.getPassportNumber());
            statement.setString(4, customer.getGender());
            statement.setDate(5, java.sql.Date.valueOf(customer.getDateOfBirth()));
            statement.setString(6, customer.getPhone());
            statement.setString(7, customer.getEmail());
            statement.setDate(8, java.sql.Date.valueOf(customer.getPassportExpiryDate()));
            statement.setInt(9, customer.getCustomerID());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // Handle the exception appropriately (log or throw a custom exception)
        }
    }


    public int getTotalCustomers() {
        String query = "SELECT COUNT(*) FROM customers";
        int totalCount = 0;

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                totalCount = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalCount;
    }

}
