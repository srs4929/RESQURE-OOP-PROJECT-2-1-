package com.example.disaster;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SignUpController {
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button signup,login,cancel;
    @FXML
    private RadioButton Admin,Client,Volunteer;
    private Alert alert;
    private PreparedStatement prepare;
    private ResultSet result;
    public void handlesignup(ActionEvent event)
    {
        String role = "";
        if (Client.isSelected()) {
            role = "Client";
        } /*else if (Admin.isSelected()) {
            role = "Admin";
        } */else if (Volunteer.isSelected()) {
            role = "Volunteer";
        }

        if (firstname.getText().isEmpty() || lastname.getText().isEmpty() || username.getText().isEmpty() || password.getText().isEmpty() || role.isEmpty()) {
            alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill up all blank fields");
            alert.showAndWait();
        }

            Database connectNow = new Database();
            Connection connectDB = connectNow.getConnection();

            String regdata="INSERT INTO users (firstname, lastname, username, password, role) VALUES (?, ?, ?, ?, ?)";


        try
        {

            prepare=connectDB.prepareStatement(regdata);
            prepare.setString(1,firstname.getText());
            prepare.setString(2,lastname.getText());
            prepare.setString(3,username.getText());
            prepare.setString(4,password.getText());
            prepare.setString(5,role);
            prepare.executeUpdate();
            alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMATION MESSAGE");
            alert.setHeaderText(null);
            alert.setContentText("Sucessfully registered");
            alert.showAndWait();
            firstname.setText("");
            lastname.setText("");
            username.setText("");
            password.setText("");
            Admin.setSelected(false);
            Client.setSelected(false);
            Volunteer.setSelected(false);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    //login button switch scene again
    public void loginpage(ActionEvent e)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception event)
        {
            event.printStackTrace();
        }
    }

    //stage cancel
    public void cancelpage(ActionEvent e)
    {
        // Close the current stage
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();

    }
}
