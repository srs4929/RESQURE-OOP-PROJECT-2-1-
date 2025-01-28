package com.example.disaster;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ReportClient {

    @FXML
    private Button home, submit;
    @FXML
    private DatePicker date;
    @FXML
    private ComboBox<String> combobox;
    private String[] division = {"Dhaka", "Khulna", "Barishal", "Chattogram", "Rajshahi", "Rangpur", "Mymensingh", "Sylhet"};
    @FXML
    private TextField username, contact,reportlocation,reportdate,disasterType;
    @FXML
    private TextArea description;
    @FXML
    private RadioButton severe, mild, moderate;
    @FXML
    private PreparedStatement prepare;
    private Alert alert;

    //back to dashboard page


    public void menu(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Client.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException event) {
            event.printStackTrace();
        }
    }

    //Current location dropdown

    public void initialize() {
        combobox.getItems().addAll(division);
        combobox.setOnAction(this::getlocation);

    }

    public void getlocation(ActionEvent event) {
        String location = combobox.getValue();
    } //division dropdown functionality ends here

    //Date picker setting
    public void getDate(ActionEvent e) {
        LocalDate mydate = date.getValue();
        System.out.println(mydate.toString());
    }

    //empty and database logic starts here
    public void handleSubmit(ActionEvent e) {
        String userName = username.getText();
        String userContact = contact.getText();
        String userLocation = combobox.getValue();
        String userDescription = description.getText();
        String disastertype = disasterType.getText();
        LocalDate reportDate = date.getValue();

        // Get selected severity level
        String severity = null;
        if (severe.isSelected()) {
            severity = "Severe";
        } else if (mild.isSelected()) {
            severity = "Mild";
        } else if (moderate.isSelected()) {
            severity = "Moderate";
        }

        // Validation for empty fields
        if (userName.isEmpty() || userContact.isEmpty() || userLocation == null || reportDate == null || severity == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill up all blank fields");
            alert.showAndWait();
            return;
        }

        // File path for saving report details
        String filePath = "OOP-Project-main/src/main/resources/disasterreport.txt";


        // Database logic to insert the report
        Database connect = new Database();
        Connection connectDB = connect.getConnection();

        String insertQuery = "INSERT INTO disaster_reports (location, disaster_type, severity, date_reported,status) VALUES (?, ?, ?, ?,?)";

        try (PreparedStatement preparedStatement = connectDB.prepareStatement(insertQuery);
             BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {

            // Insert into the database
            preparedStatement.setString(1, userLocation);
            preparedStatement.setString(2, disastertype);
            preparedStatement.setString(3, severity);
            preparedStatement.setDate(4, java.sql.Date.valueOf(reportDate));
            preparedStatement.setString(5,"pending");

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                // Write to file if database insertion succeeds
                writer.write("Report Date: " + reportDate);
                writer.newLine();
                writer.write("Name: " + userName);
                writer.newLine();
                writer.write("Contact: " + userContact);
                writer.newLine();
                writer.write("Location: " + userLocation);
                writer.newLine();
                writer.write("Disaster Type: " + disastertype);
                writer.newLine();
                writer.write("Severity: " + severity);
                writer.newLine();
                writer.write("Description: " + userDescription);
                writer.newLine();
                writer.write("--------------------------------------------------");
                writer.newLine();

                // Success message
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Successfully submitted the report to the database and saved it to the file.");
                alert.showAndWait();

                // Clear the form after submission
                username.clear();
                contact.clear();
                combobox.getSelectionModel().clearSelection();
                description.clear();
                severe.setSelected(false);
                mild.setSelected(false);
                moderate.setSelected(false);
                date.setValue(null);
            } else {
                // Show error if database insertion fails
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to submit the report to the database.");
                alert.showAndWait();
            }

        } catch (Exception ex) {
            // Handle exceptions for both database and file writing
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred: " + ex.getMessage());
            alert.showAndWait();
        }
    }


}




