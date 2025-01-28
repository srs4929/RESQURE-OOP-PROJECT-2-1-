package com.example.disaster;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;

public class ManualChat {

    @FXML
    private VBox Vbox;

    @FXML
    private ScrollPane scroll;

    @FXML
    private TextField input;

    @FXML
    private Button send,home;

    @FXML
    public void response() {
        String userMessage = input.getText();
        if (userMessage.isEmpty()) return;

        // Display user message
        displayMessage(userMessage, Pos.CENTER_RIGHT, Color.DODGERBLUE);

        // Simulate bot response
        String botResponse = getBotResponse(userMessage);
        displayMessage(botResponse, Pos.CENTER_LEFT, Color.LIGHTGRAY);

        // Clear input field
        input.clear();

        // Auto-scroll to the bottom
        scroll.setVvalue(1.0);
    }

    private void displayMessage(String message, Pos alignment, Color bubbleColor) {
        HBox messageContainer = new HBox();
        messageContainer.setAlignment(alignment);

        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(10));
        textFlow.setStyle("-fx-background-radius: 15px; -fx-background-color: " + toHexString(bubbleColor) + ";");
        text.setFill(Color.BLACK);

        messageContainer.getChildren().add(textFlow);
        Vbox.getChildren().add(messageContainer);
    }

    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private String getBotResponse(String userMessage) {
        // Print the user input to debug
        System.out.println("User message: " + userMessage);

        // Convert the user message to lower case for case-insensitive comparison
        String lowerCaseMessage = userMessage.toLowerCase();

        // Check specific phrases
        if (lowerCaseMessage.contains("i am in danger")) {
            return "I understand your urgency. Please stay calm and share your current location to receive help.";
        } else if (lowerCaseMessage.contains("we are out of food")) {
            return "I’m sending help to your location. Please provide your location and how many you need.";
        } else if (lowerCaseMessage.contains("help")) {
            return "How can I assist you during this emergency?";
        }

        // If none of the conditions match, return a default response
        return "I’m here to assist with disaster-related info. Let me know how I can help!";
    }


    public void menu(ActionEvent e) {
        try{
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


}
