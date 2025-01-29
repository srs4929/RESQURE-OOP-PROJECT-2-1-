package com.example.disaster;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {


    public Connection databaseLink;

    public Connection getConnection() {
        Connection connection = null;
        try {
            String jdbcUsername = "root";
            String jdbcPassword ="root";
            String jdbcURL = "jdbc:mysql://localhost:3306/disaster";
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }



}

