package com.example.disaster;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Survivalkit {

    @FXML
    private ComboBox<String> disaster;
    private String[] option = {"Floods", "Earthquake", "Hurricane"};

    public void initialize() {
        disaster.getItems().addAll(option);
        disaster.setOnAction(this::choosedisaster);
        need.getItems().addAll(choice);
        need.setOnAction(this::specialneed);
        water.getItems().addAll(option2);
        water.setOnAction(this::waterselection);



    }

    public void choosedisaster(ActionEvent event) {
        String selected = disaster.getValue();
    }



    @FXML
    private ComboBox<String> need;
    private String[] choice = {"Infants", "Elderly", "Pets"};


    public void specialneed(ActionEvent event) {
        String selected = disaster.getValue();
    } //need for diff people dropdown functionality ends here

    @FXML
    private ComboBox<String> water;
    private String[] option2 = {"Yes", "No"};


    public void waterselection(ActionEvent event) {
        String selected2 = water.getValue();
    } //need for water selection dropdown functionality ends here


    @FXML

    private Button ok,click,back;

    @FXML
    private Label type, special, watertext, food,waterneed,get;

    @FXML
    private TextField people, days;

    private Alert alert;

    public void suggestion(ActionEvent e) {
        String selectedDisaster = disaster.getValue();
        //suggestion type
        if (selectedDisaster != null) {
            if (selectedDisaster.equals("Floods")) {
                type.setText("Flood:Stock water, food,and first-aid.");
            } else if (selectedDisaster.equals("Earthquake")) {
                type.setText("Earthquake : Store food,tools,and first-aid.");
            } else if (selectedDisaster.equals("Hurricane")) {
                type.setText("Hurricane :Prepare water,batteries,and food.");
            } else {
                type.setText("Please select a disaster type.");
            }
        }

        //specialneed functionality

        String needy = need.getValue();
        if (needy != null) {
            if (needy.equals("Infants")) {
                special.setText("Infants :Baby formula,diapers,baby food.");
            } else if (needy.equals("Elderly")) {
                special.setText("Elderly :Extra medications,mobility aids.");
            } else if (needy.equals("Pets")) {
                special.setText("Pets:Pet food, water,leashes,comfort items");
            }
        }
        //for water suggestion
        String waterprob = water.getValue();
        if (waterprob != null) {
            if (waterprob.equals("Yes")) {
                watertext.setText("Store water for 3 days with backup(filter).");
            } else if (waterprob.equals("No")) {
                watertext.setText("Arrange clean water access using filters.");
            }
        }

        //food and water based suggestion based on day
        try {
            int numDays = Integer.parseInt(days.getText());
            int numPeople = Integer.parseInt(people.getText());
            if (numDays > 0 && numPeople > 0) {
                int foodQuantity = numDays * numPeople * 3;    // 3 meals per person per day
                int waterQuantity = numDays * numPeople * 3;  // 3 liters of water per person per day                                           // Assuming 3 meals per day per person
                food.setText("Food: Stock at least " + foodQuantity + " meals.");
                waterneed.setText("Water: Store at least " + waterQuantity + " liters.");
            } else {
                food.setText("Please enter valid numbers for days and people.");
            }
        } catch (NumberFormatException ex) {
            food.setText("Please enter valid numbers for days and people.");
        }
    }

    //successful message to get the item
    public void successful(ActionEvent e)
    {
        alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("INFORMATION MESSAGE");
        alert.setHeaderText(null);
        alert.setContentText("Sucessfully sent the item list");
        alert.showAndWait();
    }
   //back to home button
   public void home(ActionEvent e) {
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
//retrive the data





}

























