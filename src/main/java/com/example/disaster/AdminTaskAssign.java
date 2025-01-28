package com.example.disaster;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminTaskAssign {

    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, Integer> taskIdColumn;
    @FXML
    private TableColumn<Task, String> taskTitleColumn;
    @FXML
    private TableColumn<Task, String> taskDescriptionColumn;
    @FXML
    private TableColumn<Task, String> taskLocationColumn;
    @FXML
    private TableColumn<Task, String> taskTypeColumn;
    @FXML
    private TableColumn<Task, String> taskDateColumn;
    @FXML
    private TableColumn<Task, String> taskStatusColumn;

    @FXML
    private TextField taskTitleField;
    @FXML
    private TextField taskDescriptionField;
    @FXML
    private TextField taskLocationField;
    @FXML
    private TextField taskTypeField;
    @FXML
    private DatePicker taskDateField;

    @FXML
    private Button addTaskButton;

    @FXML
    private Button back;
    private final Database database = new Database();
    private ObservableList<Task> tasks = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        taskIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        taskTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        taskDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        taskLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        taskTypeColumn.setCellValueFactory(new PropertyValueFactory<>("disasterType"));
        taskDateColumn.setCellValueFactory(new PropertyValueFactory<>("assignedDate"));
        taskStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));


        loadTasksFromDatabase();


        addTaskButton.setOnAction(e -> addTask());
    }

    private void loadTasksFromDatabase() {
        tasks.clear();
        String query = "SELECT * FROM volunteer_tasks";

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                tasks.add(new Task(
                        resultSet.getInt("task_id"),
                        resultSet.getString("task_title"),
                        resultSet.getString("task_description"),
                        resultSet.getString("location"),
                        resultSet.getString("disaster_type"),
                        resultSet.getDate("assigned_date").toString(),
                        resultSet.getString("status")
                ));
            }

            taskTable.setItems(tasks);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load tasks from the database.");
        }
    }

    private void addTask() {
        String title = taskTitleField.getText();
        String description = taskDescriptionField.getText();
        String location = taskLocationField.getText();
        String disasterType = taskTypeField.getText();
        String assignedDate = taskDateField.getValue() != null ? taskDateField.getValue().toString() : null;

        if (title.isEmpty() || description.isEmpty() || location.isEmpty() || disasterType.isEmpty() || assignedDate == null) {
            showAlert("Error", "Please fill out all fields.");
            return;
        }

        String query = "INSERT INTO volunteer_tasks (task_title, task_description, location, disaster_type, assigned_date, status) " +
                "VALUES (?, ?, ?, ?, ?, 'Pending')";

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, title);
            statement.setString(2, description);
            statement.setString(3, location);
            statement.setString(4, disasterType);
            statement.setString(5, assignedDate);
            statement.executeUpdate();

            showAlert("Success", "Task added successfully!");
            clearForm();
            loadTasksFromDatabase();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to add the task.");
        }
    }

    private void clearForm() {
        taskTitleField.clear();
        taskDescriptionField.clear();
        taskLocationField.clear();
        taskTypeField.clear();
        taskDateField.setValue(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void home(ActionEvent e) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Admin.fxml"));
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
