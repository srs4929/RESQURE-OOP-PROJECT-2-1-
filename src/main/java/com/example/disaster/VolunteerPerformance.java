package com.example.disaster;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class VolunteerPerformance {

    @FXML
    private BarChart<String, Number> taskPerformanceBarChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

     @FXML
     public Button back;
    private final Database database = new Database();

    @FXML
    public void initialize() {

        xAxis.setLabel("Task Status");
        yAxis.setLabel("Number of Tasks");

        loadTaskPerformanceChart();
    }

    private void loadTaskPerformanceChart() {
        int completed = 0, pending = 0;

        String query = "SELECT status, COUNT(*) AS count " +
                "FROM volunteer_tasks WHERE status IN ('Confirmed', 'Pending') GROUP BY status";

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String status = resultSet.getString("status");
                int count = resultSet.getInt("count");

                if ("Confirmed".equalsIgnoreCase(status)) {
                    completed = count;
                } else if ("Pending".equalsIgnoreCase(status)) {
                    pending = count;
                }
            }


            System.out.println("Confirmed (Completed): " + completed);
            System.out.println("Pending: " + pending);


            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Task Performance");
            series.getData().add(new XYChart.Data<>("Completed", completed));
            series.getData().add(new XYChart.Data<>("Pending", pending));

            taskPerformanceBarChart.getData().clear();
            taskPerformanceBarChart.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load task performance data.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void home(ActionEvent e) {
        try {
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
