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

public class Client {

    @FXML
    private Button report, survive, history, contact, help;

    @FXML
    private Label firstname, lastname, role, username;
    //survivalkit page

    public void initialize() {
        // Retrieve user details from UserSession
        Usersession userSession = Usersession.getInstance();
        firstname.setText(userSession.getFirstname());
        lastname.setText(userSession.getLastname());
        username.setText(userSession.getUsername());
        role.setText(userSession.getRole());
    }

    public void survival(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Survivalkit.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException event) {
            event.printStackTrace();
        }
    }

    //report page

    public void reporting(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ReportClient.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException event) {
            event.printStackTrace();
        }
    }

    //contact page
    public void contactpage(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Important Contact.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException event) {
            event.printStackTrace();
        }


    }

    //help page
    public void ClientChat(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ClientChat.fxml"));
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




