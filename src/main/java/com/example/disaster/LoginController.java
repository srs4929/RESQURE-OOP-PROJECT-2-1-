package com.example.disaster;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;



public class LoginController {
 @FXML
 private TextField username;

 @FXML
 private PasswordField password;

 @FXML
 private  Label error;

 @FXML
    private  Button login,signup;
 @FXML
 private RadioButton Admin,Client,Volunteer;
    private PreparedStatement prepare;
    private ResultSet result;
    private Alert alert;
 public void Client(ActionEvent e)
 {    String role = "";
     if (Client.isSelected()) {
         role = "Client";
     } else if (Admin.isSelected()) {
         role = "Admin";
     } else if (Volunteer.isSelected()) {
         role = "Volunteer";
     }
     if(username.getText().isEmpty()||password.getText().isEmpty()||role.isEmpty())
     {
         error.setText("* Please fill up all the blanks");
     }
     else {
         Database connectNow = new Database();
         Connection connectDB = connectNow.getConnection();
         String selectData="SELECT username,password,role,firstname,lastname from users WHERE username= ?  and password= ? AND role = ?";
         try
         {
             prepare=connectDB.prepareStatement(selectData);
             prepare.setString(1,username.getText());
             prepare.setString(2,password.getText());
             prepare.setString(3, role);
            // prepare.setString(5,role);
             result=prepare.executeQuery();
             //if successfully login then proceed
             if(result.next())
             {
                 String userRole = result.getString("role");
                 String firstName = result.getString("firstname");
                 String lastName = result.getString("lastname");
                 //Retrive the data from the database
                 Usersession userSession = Usersession.getInstance();
                 userSession.setUserDetails(firstName, lastName, username.getText(), userRole);
                 if(userRole.equals("Client")) { //if user is client
                     try {
                         FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Client.fxml"));
                         Parent root = fxmlLoader.load();
                         Client dashboardController = fxmlLoader.getController();

                         // Pass the username and role
                         //dashboardController.setUserDetails(username.getText(), userRole,firstName,lastName);

                         Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                         Scene scene = new Scene(root);
                         stage.setScene(scene);
                         stage.show();
                     } catch (IOException event) {
                         event.printStackTrace();
                     }
                 }
                 else if(userRole.equals("Admin")) //if user is admin
                 {
                     try {
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
                 else
                 {
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
             else {   //if not successful
                 alert=new Alert(Alert.AlertType.ERROR);
                 alert.setTitle("Error message");
                 alert.setHeaderText(null);
                 alert.setContentText("Incorrect Username or Password. Try again");
                 alert.showAndWait();
             }

         }
         catch(Exception event)
         {
             event.printStackTrace();
         }
     }
 }
//if not logged in, go to signup page
 public void signuppage(ActionEvent e)
 {
     try {
         FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
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
