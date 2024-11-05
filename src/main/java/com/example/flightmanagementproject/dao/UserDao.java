package com.example.flightmanagementproject.dao;

import com.example.flightmanagementproject.models.Customer;
import com.example.flightmanagementproject.models.User;

import java.sql.*;

public class UserDao {

    public User login(String username, String password) {
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection connection = dbConnection.getConnection();

            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String role = resultSet.getBoolean("isAdmin") ? "admin" : "customer";
                    int id = resultSet.getInt("id");
                    return new User(id,username, role, resultSet.getBoolean("isAdmin"));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPasswordByUsername(String username) {
        String password = null;
        String sql = "SELECT password FROM Users WHERE username = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    password = resultSet.getString("password");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return password;
    }

    public void updatePassword(int userId, String newPassword) {
        String sql = "UPDATE Users SET password = ? WHERE id = ?";
        Connection connection = new DatabaseConnection().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newPassword);
            statement.setInt(2, userId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registerUser(User user, Customer customer) throws SQLException {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connection = dbConnection.getConnection();

        String userQuery = "INSERT INTO Users (username, password, isAdmin) VALUES (?, ?, 0);";
        try (PreparedStatement userStatement = connection.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS)) {
            userStatement.setString(1, user.getUsername());
            userStatement.setString(2, user.getPassword());
            userStatement.executeUpdate();

            ResultSet userResultSet = userStatement.getGeneratedKeys();
            if (userResultSet.next()) {
                int userID = userResultSet.getInt(1);

                String customerQuery = "INSERT INTO Customers (UserID, Name, Nationality, PassportNumber, Picture, Gender, DateOfBirth, Phone, Email, passport_expiry_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
                try (PreparedStatement customerStatement = connection.prepareStatement(customerQuery)) {
                    customerStatement.setInt(1, userID);
                    customerStatement.setString(2, customer.getName());
                    customerStatement.setString(3, customer.getNationality());
                    customerStatement.setString(4, customer.getPassportNumber());
                    customerStatement.setString(5, customer.getPicture());
                    customerStatement.setString(6, customer.getGender());
                    customerStatement.setDate(7, Date.valueOf(customer.getDateOfBirth()));
                    customerStatement.setString(8, customer.getPhone());
                    customerStatement.setString(9, customer.getEmail());
                    customerStatement.setDate(10, Date.valueOf(customer.getPassportExpiryDate()));

                    customerStatement.executeUpdate();
                }
            }
        }
    }
}
