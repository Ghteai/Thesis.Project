package com.example.flightmanagementproject.dao;

import com.example.flightmanagementproject.models.Flight;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FlightsDao {
    private DatabaseConnection databaseConnection;

    public FlightsDao() {
        this.databaseConnection = new DatabaseConnection();
    }

    public List<Flight> getAllFlights() {
        List<Flight> flights = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection()) {
            String query = "SELECT * FROM flights";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Flight flight = new Flight(
                            resultSet.getInt("FlightID"),
                            resultSet.getString("FlightNumber"),
                            resultSet.getString("FromLocation"),
                            resultSet.getString("ToLocation"),
                            resultSet.getDate("DepartureDateTime").toLocalDate(),
                            resultSet.getString("DepartureTime"),
                            resultSet.getDouble("Price"),
                            resultSet.getInt("AvailableSeats")
                    );

                    flights.add(flight);

                    // Print information for debugging
                    System.out.println("Flight ID: " + flight.getFlightID());
                    System.out.println("Flight Number: " + flight.getFlightNumber());
                    System.out.println("From Location: " + flight.getFromLocation());
                    System.out.println("To Location: " + flight.getToLocation());
                    System.out.println("Departure DateTime: " + flight.getDepartureDateTime());
                    System.out.println("Price: " + flight.getPrice());
                    System.out.println("Available Seats: " + flight.getAvailableSeats());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flights;
    }

    public void addFlight(Flight flight) {
        try (Connection connection = databaseConnection.getConnection()) {
            String query = "INSERT INTO flights (FlightNumber, FromLocation, ToLocation, DepartureDateTime, Price, AvailableSeats , DepartureTime) VALUES (?, ?, ?, ?, ?, ? , ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, flight.getFlightNumber());
                statement.setString(2, flight.getFromLocation());
                statement.setString(3, flight.getToLocation());
                statement.setDate(4, Date.valueOf(flight.getDepartureDateTime()));
                statement.setDouble(5, flight.getPrice());
                statement.setInt(6, flight.getAvailableSeats());
                statement.setString(7 , flight.getDepartureTime());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateFlight(Flight flight) {
        String sql = "UPDATE flights SET flightNumber = ?, fromLocation = ?, toLocation = ?, " +
                "departureDateTime = ?, price = ?, availableSeats = ?  , DepartureTime = ? WHERE flightID = ?";
        Connection connection = databaseConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, flight.getFlightNumber());
            statement.setString(2, flight.getFromLocation());
            statement.setString(3, flight.getToLocation());
            statement.setDate(4, Date.valueOf(flight.getDepartureDateTime()));
            statement.setDouble(5, flight.getPrice());
            statement.setInt(6, flight.getAvailableSeats());
            statement.setString(7 , flight.getDepartureTime());
            statement.setInt(8, flight.getFlightID());



            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // Handle the exception appropriately (log or throw a custom exception)
        }
    }

    public Flight getFlightById(int flightId) throws SQLException {
        Flight flight = null;

        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        String sql = "SELECT * FROM Flights WHERE flightId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, flightId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    flight = new Flight();
                    flight.setFlightID(resultSet.getInt("flightId"));
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



    public void deleteFlight(int flightId) {
        String sql = "DELETE FROM flights WHERE flightID = ?";
        Connection connection = databaseConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, flightId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // Handle the exception appropriately (log or throw a custom exception)
        }
    }


    public Flight getFlightByNumber(String flightNumber) {
        String query = "SELECT * FROM flights WHERE FlightNumber = ?";
        Flight foundFlight = null;

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, flightNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    foundFlight = new Flight(
                            resultSet.getInt("FlightID"),
                            resultSet.getString("FlightNumber"),
                            resultSet.getString("FromLocation"),
                            resultSet.getString("ToLocation"),
                            resultSet.getDate("DepartureDateTime").toLocalDate(),
                            resultSet.getString("DepartureTime"),
                            resultSet.getDouble("Price"),
                            resultSet.getInt("AvailableSeats")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately (log or throw a custom exception)
        }

        return foundFlight;
    }

    public int getTotalFlightsCount() {
        String query = "SELECT COUNT(*) FROM flights";
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

    public int getFlightsCountForToday() {
        int count = 0;
        String query = "SELECT COUNT(*) FROM flights WHERE DATE(DepartureDateTime) = CURDATE()";

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

    public int getFlightsCountForTomorrow() {
        int count = 0;
        String query = "SELECT COUNT(*) FROM flights WHERE DATE(DepartureDateTime) = CURDATE() + INTERVAL 1 DAY";

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

    public int getFlightsCountForThisWeek() {
        int count = 0;
        String query = "SELECT COUNT(*) FROM flights WHERE YEARWEEK(DepartureDateTime) = YEARWEEK(NOW())";

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
