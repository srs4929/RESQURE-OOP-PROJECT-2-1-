package com.example.disaster;
//Controller for VolunteerTask.fxml
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class VolunteerTasks  extends Task {

    @FXML
    private FlowPane taskFlowPane;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private Button submitReportButton;


    private final Database database = new Database();

    // File path for saving report details
    private final String filePath = "src/main/resources/volunteerreport.txt";


    public VolunteerTasks() {
        super();
    }
    @FXML
    public void initialize() {

        loadTasksFromDatabase();


        searchButton.setOnAction(e -> searchTasks());


        submitReportButton.setOnAction(e -> submitReport());
    }

    private void loadTasksFromDatabase() {
        taskFlowPane.getChildren().clear(); // Clear existing cards

        String query = "SELECT task_id, task_title, task_description, location, assigned_date, disaster_type, status FROM volunteer_tasks";  // from schema project table volunteer_tasks

        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Iterate through the result set and create task cards
            while (resultSet.next()) {
                String taskId = resultSet.getString("task_id");
                String title = resultSet.getString("task_title");
                String description = resultSet.getString("task_description");
                String location = resultSet.getString("location");
                String assignedDate = resultSet.getDate("assigned_date").toString();
                String disasterType = resultSet.getString("disaster_type");
                String status = resultSet.getString("status");

                addTaskCard(taskId, title, description, location, assignedDate, disasterType, status);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load tasks from the database.", Alert.AlertType.ERROR);
        }
    }

    private void addTaskCard(String taskId, String title, String description, String location, String assignedDate, String disasterType, String status) {
        VBox taskCard = new VBox();
        taskCard.setStyle("-fx-background-color: #F7F8FA; -fx-border-color: #D0D3D9; -fx-border-radius: 10; -fx-padding: 10;");
        taskCard.setPrefWidth(666);
        taskCard.setPrefHeight(200);

        Label titleLabel = new Label("Title: " + title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-padding: 8px;");

        Label descriptionLabel = new Label("Description: " + description);
        descriptionLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        Label locationLabel = new Label("Location: " + location);
        locationLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        Label dateLabel = new Label("Assigned Date: " + assignedDate);
        dateLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        Label disasterTypeLabel = new Label("Disaster: " + disasterType);
        disasterTypeLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        Label statusLabel = new Label("Status: " + status);
        statusLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        // Confirm Button
        Button confirmButton = new Button("Confirm");
        confirmButton.setStyle("-fx-background-color: #0ab74f; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px; -fx-pref-width: 80px; -fx-padding: 3px;");
        confirmButton.setOnAction(e -> updateTaskStatus(taskId, "Confirmed"));

//        // Reject Button
//        Button rejectButton = new Button("Reject");
//        rejectButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px; -fx-pref-width: 80px; -fx-padding: 3px;");
//        rejectButton.setOnAction(e -> updateTaskStatus(taskId, "Rejected"));

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(confirmButton);

        taskCard.getChildren().addAll(titleLabel, descriptionLabel, locationLabel, dateLabel, disasterTypeLabel, statusLabel, buttonBox);
        taskFlowPane.getChildren().add(taskCard);
    }

    private void updateTaskStatus(String taskId, String newStatus) {
        String updateQuery = "UPDATE volunteer_tasks SET status = ? WHERE task_id = ?";

        try (Connection connection = database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {


            preparedStatement.setString(1, newStatus);
            preparedStatement.setString(2, taskId);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                showAlert("Success", "Task status updated to: " + newStatus, Alert.AlertType.INFORMATION);

                loadTasksFromDatabase();
            } else {
                showAlert("Error", "Failed to update the task status.", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update the task status in the database.", Alert.AlertType.ERROR);
        }
    }

    private void searchTasks() {
        String query = searchField.getText().toLowerCase(); // Get the user input from the search field

        taskFlowPane.getChildren().clear(); // Clear existing cards


        String sql = "SELECT task_id, task_title, task_description, location, assigned_date, status, disaster_type FROM volunteer_tasks " +
                "WHERE LOWER(location) LIKE '%" + query + "%'";

        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {


            while (resultSet.next()) {
                String taskId = resultSet.getString("task_id");
                String title = resultSet.getString("task_title");
                String description = resultSet.getString("task_description");
                String location = resultSet.getString("location");
                String assignedDate = resultSet.getDate("assigned_date").toString();
                String disasterType = resultSet.getString("disaster_type");
                String status = resultSet.getString("status");


                addTaskCard(taskId, title, description, location, assignedDate, disasterType, status);
            }


            if (taskFlowPane.getChildren().isEmpty()) {
                showAlert("Search Results", "No tasks found for the entered location!", Alert.AlertType.INFORMATION);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to search tasks by location in the database.", Alert.AlertType.ERROR);
        }
    }

    private void submitReport() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Submit Report");
        alert.setHeaderText("Write your report below:");


        TextArea reportArea = new TextArea();
        reportArea.setPromptText("Enter your report here...");
        reportArea.setPrefWidth(400);
        reportArea.setPrefHeight(150);


        alert.getDialogPane().setPrefWidth(500);
        alert.getDialogPane().setPrefHeight(300);
        alert.getDialogPane().setContent(reportArea);


        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String reportText = reportArea.getText();

                if (reportText.isEmpty()) {
                    showAlert("Error", "Report cannot be empty!", Alert.AlertType.ERROR);
                } else {
                    writeReportToFile(reportText);
                }
            }
        });
    }


    private void writeReportToFile(String reportText) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write("Volunteer Report:");
            writer.newLine();
            writer.write(reportText);
            writer.newLine();
            writer.write("--------------------------------------------------");
            writer.newLine();

            showAlert("Success", "Report submitted successfully!", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void handleHome(ActionEvent e) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Volunteer.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException event) {
            event.printStackTrace();
        }
    }
}
