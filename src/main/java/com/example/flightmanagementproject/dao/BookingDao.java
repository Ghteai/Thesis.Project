package com.example.flightmanagementproject.dao;

import com.example.flightmanagementproject.models.Booking;
import com.example.flightmanagementproject.models.Customer;
import com.example.flightmanagementproject.models.Flight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDao {
    public void saveBooking(Booking booking) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        String sql = "INSERT INTO Booking (reference_number, customer_id, flight_id, class, extra_leg_room, final_price, booking_time) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, booking.getReferenceNumber());
            preparedStatement.setInt(2, booking.getCustomerId());
            preparedStatement.setInt(3, booking.getFlightId());
            preparedStatement.setString(4, booking.getClassType());
            preparedStatement.setBoolean(5, booking.isExtraLegRoom());
            preparedStatement.setBigDecimal(6, booking.getFinalPrice());
            preparedStatement.setObject(7, booking.getBookingTime());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int bookingId = generatedKeys.getInt(1);
                    updateFlightAvailableSeats(connection, booking.getFlightId());
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int getTotalBookingsCount() throws SQLException {
        int totalCount = 0;
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        String sql = "SELECT COUNT(*) FROM Booking";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                totalCount = resultSet.getInt(1);
            }
        }

        return totalCount;
    }
    public double getTotalRevenue() throws SQLException {
        double totalRevenue = 0.0;
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        String sql = "SELECT SUM(final_price) FROM Booking";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                totalRevenue = resultSet.getDouble(1);
            }
        }

        return totalRevenue;
    }


    private void updateFlightAvailableSeats(Connection connection, int flightId) {
        try {
            String updateSql = "UPDATE Flight SET available_seats = available_seats - 1 WHERE flight_id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                updateStatement.setInt(1, flightId);
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> bookingsList = new ArrayList<>();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        String sql = "SELECT b.*, f.flightNumber, c.name as customerName " +
                "FROM Booking b " +
                "JOIN Flights f ON b.flight_id = f.flightId " +
                "JOIN Customers c ON b.customer_id = c.customerId";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Booking booking = extractBookingFromResultSet(resultSet);
                String customerName = resultSet.getString("customerName");
                booking.setCustomerName(customerName);
                bookingsList.add(booking);
            }
        }
        return bookingsList;
    }


    public List<Booking> getAllBookingsForCustomer(int customerId) throws SQLException {
        List<Booking> bookingsList = new ArrayList<>();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        String sql = "SELECT b.*, f.flightNumber FROM Booking b " +
                "JOIN Flights f ON b.flight_id = f.flightId " +
                "WHERE b.customer_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, customerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Booking booking = extractBookingFromResultSet(resultSet);
                    bookingsList.add(booking);
                }
            }
        }
        return bookingsList;
    }

    private Booking extractBookingFromResultSet(ResultSet resultSet) throws SQLException {
        Booking booking = new Booking();
        booking.setReferenceNumber(resultSet.getString("reference_number"));
        booking.setCustomerId(resultSet.getInt("customer_id"));
        booking.setFlightId(resultSet.getInt("flight_id"));
        booking.setFlightNumber(resultSet.getString("flightNumber"));
        booking.setClassType(resultSet.getString("class"));
        booking.setExtraLegRoom(resultSet.getBoolean("extra_leg_room"));
        booking.setFinalPrice(resultSet.getBigDecimal("final_price"));
        booking.setBookingTime(resultSet.getObject("booking_time", LocalDateTime.class));
        // Add other fields as needed

        return booking;
    }

    private Booking getBookingById(String ref) throws SQLException {
        Booking booking = null;

        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        String sql = "SELECT * FROM Booking WHERE reference_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, ref);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    booking = new Booking();
                    booking.setReferenceNumber(resultSet.getString("reference_number"));
                    // Set other fields as needed
                }
            }
        }

        return booking;
    }

    public Booking getBookingWithDetails(String ref) throws SQLException {
        Booking booking = getBookingById(ref);

        if (booking != null) {
            Flight flight = getFlightById(booking.getFlightId());
            Customer customer = getCustomerById(booking.getCustomerId());

            booking.setFlight(flight);
            booking.setCustomer(customer);
        }

        return booking;
    }


    public Flight getFlightById(int flightId) throws SQLException {
        Flight flight = null;

        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        String sql = "SELECT * FROM Flights WHERE flightID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, flightId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    flight = new Flight();
                    flight.setFlightID(resultSet.getInt("flightID"));
                    flight.setFlightNumber(resultSet.getString("flightNumber"));
                    flight.setFromLocation(resultSet.getString("fromLocation"));
                    flight.setToLocation(resultSet.getString("toLocation"));
                    flight.setDepartureDateTime(resultSet.getDate("departureDateTime").toLocalDate());
                    flight.setDepartureTime(resultSet.getString("departureTime"));
                    flight.setPrice(resultSet.getDouble("price"));
                    flight.setAvailableSeats(resultSet.getInt("availableSeats"));
                }
            }
        }

        return flight;
    }

    public Customer getCustomerById(int customerId) throws SQLException {
        Customer customer = null;

        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        String sql = "SELECT * FROM Customers WHERE customerID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, customerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    customer = new Customer();
                    customer.setCustomerID(resultSet.getInt("customerID"));
                    customer.setName(resultSet.getString("name"));
                    customer.setNationality(resultSet.getString("nationality"));
                    customer.setPassportNumber(resultSet.getString("passportNumber"));
                    customer.setPicture(resultSet.getString("picture"));
                }
            }
        }

        return customer;
    }

    public void deleteBooking(String ref) throws SQLException {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();
        String sql = "DELETE FROM Booking WHERE reference_number = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, ref);
        preparedStatement.executeUpdate();
        System.out.println("booking with reference : "+ref+" has been deleted");
    }

}
