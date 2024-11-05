package com.example.flightmanagementproject.controllers.user;

import com.example.flightmanagementproject.dao.BookingDao;
import com.example.flightmanagementproject.dao.CustomerDao;
import com.example.flightmanagementproject.dao.FlightsDao;
import com.example.flightmanagementproject.models.Booking;
import com.example.flightmanagementproject.models.Customer;
import com.example.flightmanagementproject.models.Flight;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class BookingPrintController {
    private Booking booking;
    private Flight flight;
    private Customer customer;

    @FXML
    private Label departureTimeLabel;

    @FXML
    private Label fromLabel;

    @FXML
    private Label toLabel;

    @FXML
    private Label bookingRefNumberLabel;

    @FXML
    private Label flightNumberLabel;

    @FXML
    private Label nationalityLabel;

    @FXML
    private Label fullNameLabel;

    @FXML
    private Label fullNameLabel1;

    @FXML
    private Label passportNumberLabel;

    @FXML
    private Label bookingDateLabel;

    @FXML
    private Label classLabel;

    @FXML
    private Label extraLegRoomLabel;

    @FXML
    private Label bookingPriceLabel;
    FlightsDao flightsDao ;
    CustomerDao customerDao;
    BookingDao bookingDao;

    public BookingPrintController(){
        flightsDao = new FlightsDao();
        customerDao = new CustomerDao();
        bookingDao = new BookingDao();
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }


    public void initializeData() throws SQLException {
        flight = flightsDao.getFlightById(booking.getFlightId());
        customer = customerDao.getCustomerById(booking.getCustomerId());

        departureTimeLabel.setText("Departure time: " + flight.getDepartureTime());
        fromLabel.setText("From: " + flight.getFromLocation());
        toLabel.setText("To: " + flight.getToLocation());
        flightNumberLabel.setText("Flight Number: " + flight.getFlightNumber());
        nationalityLabel.setText("Nationality: " + customer.getNationality());
        fullNameLabel.setText("Full Name: " + customer.getName());
        fullNameLabel1.setText("Full Name: " + customer.getName());
        bookingRefNumberLabel.setText("Booking Reference Number : "+booking.getReferenceNumber());
        passportNumberLabel.setText("Passport Number: " + customer.getPassportNumber());
        bookingDateLabel.setText("Date of Booking: " + booking.getBookingTime().toString());
        classLabel.setText("Class: " + booking.getClassType());
        if(booking.isExtraLegRoom())
            extraLegRoomLabel.setText("Extra Leg Room: YES");
        else
            extraLegRoomLabel.setText("Extra Leg Room: NO");

        bookingPriceLabel.setText("Booking Price: " + booking.getFinalPrice().toString()+"$");
    }


    @FXML
    public void print() throws SQLException, IOException {
        flight = flightsDao.getFlightById(booking.getFlightId());
        customer = customerDao.getCustomerById(booking.getCustomerId());

        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream("E-ticket_" + booking.getReferenceNumber() + ".pdf"));
            doc.open();

            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Phrase title = new Phrase("E-Ticket - Booking Information", titleFont);
            com.itextpdf.text.pdf.PdfPTable titleTable = new com.itextpdf.text.pdf.PdfPTable(1);
            titleTable.setWidthPercentage(100);
            titleTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            titleTable.addCell(title);
            doc.add(titleTable);

            doc.add(new Paragraph("Departure time: " + flight.getDepartureTime()));
            doc.add(new Paragraph("From: " + flight.getFromLocation()));
            doc.add(new Paragraph("To: " + flight.getToLocation()));
            doc.add(new Paragraph("Flight Number: " + flight.getFlightNumber()));
            doc.add(new Paragraph("Full Name: " + customer.getName()));
            doc.add(new Paragraph("Nationality: " + customer.getNationality()));
            doc.add(new Paragraph("Booking Reference Number: " + booking.getReferenceNumber()));
            doc.add(new Paragraph("Passport Number: " + customer.getPassportNumber()));
            doc.add(new Paragraph("Date of Booking: " + booking.getBookingTime().toString()));
            doc.add(new Paragraph("Class: " + booking.getClassType()));
            doc.add(new Paragraph("Extra Leg Room: " + (booking.isExtraLegRoom() ? "YES" : "NO")));
            doc.add(new Paragraph("Booking Price: " + booking.getFinalPrice().toString() + "$"));

            java.awt.Desktop.getDesktop().open(new File("E-ticket_" + booking.getReferenceNumber() + ".pdf"));
            closeWindow();
        } catch (DocumentException e) {
            throw new RuntimeException("Error creating PDF", e);
        } finally {
            if (doc != null) {
                doc.close();
            }
        }
    }

    @FXML
    public void cancelBooking() throws SQLException {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText("Cancel Booking");
        confirmationDialog.setContentText("Are you sure you want to cancel this booking?");

        confirmationDialog.initModality(Modality.APPLICATION_MODAL);

        Optional<ButtonType> result = confirmationDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            bookingDao.deleteBooking(booking.getReferenceNumber());

            Alert informationDialog = new Alert(Alert.AlertType.INFORMATION);
            informationDialog.setTitle("Information");
            informationDialog.setHeaderText("Booking Canceled");
            informationDialog.setContentText("The booking has been canceled successfully.");

            informationDialog.initModality(Modality.APPLICATION_MODAL);
            informationDialog.show();
            closeWindow();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) departureTimeLabel.getScene().getWindow();
        stage.close();
    }
}
