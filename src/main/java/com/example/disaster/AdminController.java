package com.example.disaster;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {

    @FXML
    private Button Dashboard,AssignTasks,help;


    public void AdminDashboard(ActionEvent e){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException event) {
            event.printStackTrace();
        }
    }

    public void AdminTaskAssign(ActionEvent e){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdminTaskAssign.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException event) {
            event.printStackTrace();
        }
    }

    public void AdminChat(ActionEvent event) {
        try {
            // Load the AdminChat.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdminChat.fxml"));
            Parent root = fxmlLoader.load();

            // Get the AdminChatController instance from the FXML loader
            AdminChatController adminChatController = fxmlLoader.getController();
            if (adminChatController == null) {
                System.out.println("Error: AdminChatController is null after loading FXML.");
            } else {
                System.out.println("AdminChatController instance retrieved successfully.");
            }

            // Register the AdminChatController with the ChatServer
//            ChatServer.setAdminChatController(adminChatController);
//            System.out.println("AdminChatController is now set in ChatServer.");

            // Switch to the Admin Chat scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Admin Chat Panel opened successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading AdminChat.fxml: " + e.getMessage());
        }
    }

}
