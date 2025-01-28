package com.example.disaster;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class ClientChatController {

    @FXML
    private VBox chatArea; // Changed from TextArea to VBox for message bubbles

    @FXML
    private TextField messageField;

    @FXML
    private Button sendButton,home;

    private Socket clientSocket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String clientName;

    @FXML
    public void initialize() {

        connectServer();


        sendButton.setOnAction(event -> sendMessage());
    }

    private void connectServer() {
        try {
            clientSocket = new Socket("localhost", 6666);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            Usersession userSession = Usersession.getInstance();
            clientName = userSession.getUsername();

            writer.write(clientName + "\n");
            writer.flush();

            //addMessageBubble("Connected to server as " + clientName, false);

            // Start a thread to listen for incoming messages
            new Thread(new Runnable() {
                @Override
                public void run() {
                    listenMessage();
                }
            }).start();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void listenMessage() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                String incomingMessage = message;
                // to update the UI
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        addMessageBubble(incomingMessage, false);
                    }
                });
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void sendMessage() {
        try {
            String message = messageField.getText();
            if (!message.isEmpty()) {
                writer.write(message + "\n");
                writer.flush();
                addMessageBubble(clientName + ": " + message, true);
                messageField.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addMessageBubble(String message, boolean isClient) {
        String[] parts = message.split(":", 2);
        String clientName;
        String chatMessage;
        if (parts.length == 2) {
            clientName = parts[0].trim();
            chatMessage = parts[1].trim();
        }else{
            chatMessage = parts[0].trim();
        }

        HBox messageBubble = new HBox();
        messageBubble.setSpacing(10);

        Label messageLabel = new Label(chatMessage);
        messageLabel.setWrapText(true);


        if (isClient) {
            messageLabel.setStyle("-fx-background-color: #64aced; -fx-text-fill: white; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10;");
            messageBubble.setStyle("-fx-alignment: CENTER_RIGHT;");
        } else {
            messageLabel.setStyle("-fx-background-color: #e1e1e1; -fx-text-fill: black; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10;");
            messageBubble.setStyle("-fx-alignment: CENTER_LEFT;");
        }

        messageBubble.getChildren().add(messageLabel);
        chatArea.getChildren().add(messageBubble);
    }

    public void disconnect() {
        try {
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //back to home page

    public void menu(ActionEvent e) {
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
}


