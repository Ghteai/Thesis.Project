package com.example.flightmanagementproject.dao;

import com.example.flightmanagementproject.models.Complaint;
import com.example.flightmanagementproject.models.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDao {
    private DatabaseConnection databaseConnection;

    public ComplaintDao() {
        databaseConnection = new DatabaseConnection();
    }

    public void addComplaint(Customer customer, String type, String title, String description) {
        Connection connection = databaseConnection.getConnection();
        String query = "INSERT INTO complaints (customer_id, type, title, description) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customer.getCustomerID());
            preparedStatement.setString(2, type);
            preparedStatement.setString(3, title);
            preparedStatement.setString(4, description);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Complaint> getAllComplaints() {
        List<Complaint> complaints = new ArrayList<>();
        String sql = "SELECT c.*, cust.name AS customer_name FROM Complaints c " +
                "JOIN Customers cust ON c.customer_id = cust.CustomerID";
        try (Connection connection = databaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Complaint complaint = createComplaintFromResultSet(resultSet);
                complaints.add(complaint);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return complaints;
    }

    private Complaint createComplaintFromResultSet(ResultSet resultSet) throws SQLException {
        Complaint complaint = new Complaint();
        complaint.setComplaintId(resultSet.getInt("complaint_id"));
        complaint.setType(resultSet.getString("Type"));
        complaint.setTitle(resultSet.getString("Title"));
        complaint.setDescription(resultSet.getString("Description"));
        complaint.setCustomerName(resultSet.getString("customer_name"));

        return complaint;
    }


    public int getTotalComplaints() {
        int count = 0;
        String query = "SELECT COUNT(*) complaints;";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }
}
