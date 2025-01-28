package com.example.disaster;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class AdminDashboard {

    @FXML
    private Button Homebutton;

    @FXML
    private TableView<DisasterReport> tableView;

    @FXML
    private TableColumn<DisasterReport, Integer> reportIdColumn;

    @FXML
    private TableColumn<DisasterReport, String> disasterTypeColumn;

    @FXML
    private TableColumn<DisasterReport, String> locationColumn;

    @FXML
    private TableColumn<DisasterReport, String> severityColumn;

    @FXML
    private TableColumn<DisasterReport, String> dateReportedColumn;

    @FXML
    private TableColumn<DisasterReport, String> statusColumn;

    @FXML
    private TableColumn<DisasterReport, Void> actionColumn;

    @FXML
    private TextField searchBar;

    @FXML
    private BarChart<String, Number> barChart1;

    @FXML
    private BarChart<String, Number> barChart2;

    @FXML
    private Label currentDateLabel;

    private ObservableList<DisasterReport> disasterReports = FXCollections.observableArrayList();

    private final Database database;

    // Constructor to initialize Database
    public AdminDashboard() {
        this.database = new Database(); // Initialize the Database instance
    }

    @FXML
    public void initialize() {
        // Configure TableView columns
        reportIdColumn.setCellValueFactory(new PropertyValueFactory<>("reportId"));
        disasterTypeColumn.setCellValueFactory(new PropertyValueFactory<>("disasterType"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        severityColumn.setCellValueFactory(new PropertyValueFactory<>("severity"));
        dateReportedColumn.setCellValueFactory(new PropertyValueFactory<>("dateReported"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));


        addActionButtons();


        loadTableData();
        loadBarChart1();
        loadBarChart2();


        currentDateLabel.setText("Date: " + java.time.LocalDate.now().toString());


        setupSearch();
    }



    public void addActionButtons() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button verifyButton = new Button("Verify");
            private final Button rejectButton = new Button("Reject");

            {
                verifyButton.setOnAction(event -> {
                    DisasterReport report = getTableView().getItems().get(getIndex());
                    updateReportStatus(report.getReportId(), "Verified");
                });

                rejectButton.setOnAction(event -> {
                    DisasterReport report = getTableView().getItems().get(getIndex());
                    updateReportStatus(report.getReportId(), "Rejected");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, verifyButton, rejectButton);
                    setGraphic(buttons);
                }
            }
        });
    }

    public void updateReportStatus(int reportId, String newStatus) {
        String updateQuery = "UPDATE disaster_reports SET status = ? WHERE report_id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, reportId);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Report ID " + reportId + " updated to " + newStatus);
                loadTableData(); // Refresh the table data
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update report status.", Alert.AlertType.ERROR);
        }
    }

    public void loadTableData() {
        disasterReports.clear();
        String query = "SELECT * FROM disaster_reports";

        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                disasterReports.add(new DisasterReport(
                        resultSet.getInt("report_id"),
                        resultSet.getString("location"),
                        resultSet.getString("disaster_type"),
                        resultSet.getString("severity"),
                        resultSet.getString("status"),
                        resultSet.getDate("date_reported").toString()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load data from the database.", Alert.AlertType.ERROR);
        }

        tableView.setItems(disasterReports);
    }

    public void setupSearch() {
        FilteredList<DisasterReport> filteredData = new FilteredList<>(disasterReports, b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(report -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return report.getDisasterType().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        SortedList<DisasterReport> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }

    public void loadBarChart1() {
        barChart1.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Disasters by Location");

        String query = "SELECT location, COUNT(*) AS count FROM disaster_reports GROUP BY location";

        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String location = resultSet.getString("location");
                int count = resultSet.getInt("count");
                series.getData().add(new XYChart.Data<>(location, count));
            }

            barChart1.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load bar chart data.", Alert.AlertType.ERROR);
        }
    }

    public void loadBarChart2() {
        barChart2.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Disaster Percentages");

        String totalQuery = "SELECT COUNT(*) AS total FROM disaster_reports";
        String typeQuery = "SELECT disaster_type, COUNT(*) AS count FROM disaster_reports GROUP BY disaster_type";

        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement()) {

            int total = 0;
            try (ResultSet resultSet = statement.executeQuery(totalQuery)) {
                if (resultSet.next()) {
                    total = resultSet.getInt("total");
                }
            }

            try (ResultSet resultSet = statement.executeQuery(typeQuery)) {
                while (resultSet.next()) {
                    String type = resultSet.getString("disaster_type");
                    int count = resultSet.getInt("count");
                    double percentage = ((double) count / total) * 100;
                    series.getData().add(new XYChart.Data<>(type, percentage));
                }
            }

            barChart2.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load bar chart data.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleHome(ActionEvent e) {
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
