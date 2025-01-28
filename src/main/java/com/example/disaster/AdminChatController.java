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


public class AdminChatController {

    @FXML
    private ListView<String> clientListView;

    @FXML
    private VBox chatArea;

    @FXML
    private TextField messageField;

    @FXML
    private Button sendButton,home;

    private Socket adminSocket;
    private BufferedReader reader;
    private BufferedWriter writer;

    private Map<String, StringBuilder> clientConversations = new HashMap<>();
    private String selectedClient;

    @FXML
    public void initialize() {
        connectServer();

        sendButton.setOnAction(event -> sendMessage());

        clientListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedClient = newValue;
                updateChatArea(); // Update the chat area to show the conversation with the selected client
            }
        });
    }

    private void connectServer() {
        try {
            adminSocket = new Socket("localhost", 6666);
            reader = new BufferedReader(new InputStreamReader(adminSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(adminSocket.getOutputStream()));

            writer.write("Admin\n");
            writer.flush();

            System.out.println("Connected to server as Admin");

            // Start a thread to listen for messages from the server
            new Thread(new Runnable() {
                @Override
                public void run() {
                    listenMessage();
                }
            }).start();

        } catch (IOException e) {
            System.out.println("Failed to connect to the server.");
            e.printStackTrace();
        }
    }

    private void listenMessage() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                String Message = message;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        handleMessage(Message);
                    }
                });
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void handleMessage(String message) {
        // If it's a new client connection
        if (message.endsWith("has connected.")) {
            String clientName = message.split(" ")[0];
            if (!clientListView.getItems().contains(clientName)) {
                clientListView.getItems().add(clientName);
                clientConversations.put(clientName, new StringBuilder());
            }
        } else {
            // If it's a chat message, add it to the corresponding client's conversation
            String[] parts = message.split(":", 2);
            if (parts.length == 2) {
                String clientName = parts[0].trim();
                String chatMessage = parts[1].trim();

                // Append the message to the client's conversation
                clientConversations.computeIfAbsent(clientName, k -> new StringBuilder()).append(clientName).append(": ").append(chatMessage).append("\n");


                if (clientName.equals(selectedClient)) {
                    addMessageBubble(clientName, chatMessage, false);
                }
            }
        }
    }

    private void sendMessage() {
        String message = messageField.getText();

        if (selectedClient != null && !message.isEmpty()) {
            try {
                // Send the message to the server
                writer.write(selectedClient + ":" + message + "\n");
                writer.flush();

                // Append the message to the current client's conversation
                clientConversations.computeIfAbsent(selectedClient, k -> new StringBuilder()).append("Admin: ").append(message).append("\n");


                addMessageBubble("Admin", message, true);
                messageField.clear();
            } catch (IOException e) {
                System.out.println("Failed to send message.");
                e.printStackTrace();
            }
        } 
    }

    private void updateChatArea() {

        chatArea.getChildren().clear();
        if (selectedClient != null) {
            StringBuilder conversation = clientConversations.get(selectedClient);
            if (conversation != null) {
                String[] messages = conversation.toString().split("\n");
                for (String msg : messages) {
                    if (msg.startsWith("Admin: ")) {
                        addMessageBubble("Admin", msg.replace("Admin: ", ""), true);
                    } else {
                        String[] parts = msg.split(": ", 2);
                        if (parts.length == 2) {
                            addMessageBubble(parts[0], parts[1], false);
                        }
                    }
                }
            }
        }
    }

    private void addMessageBubble(String sender, String message, boolean isAdmin) {
        HBox messageBubble = new HBox();
        messageBubble.setSpacing(10);

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        if (isAdmin) {
            messageLabel.setStyle("-fx-background-color: #64aced; -fx-text-fill: white; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10;");
            messageBubble.setStyle("-fx-alignment: CENTER_RIGHT;");
        } else {
            messageLabel.setStyle("-fx-background-color: #e1e1e1; -fx-text-fill: black; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10;");
            messageBubble.setStyle("-fx-alignment: CENTER_LEFT;");
        }

        if (isAdmin) {
            messageBubble.getChildren().add(messageLabel); // Admin's messages align right
            messageBubble.setStyle("-fx-alignment: CENTER_RIGHT;");
        } else {
            Label senderLabel = new Label(sender + ": ");
            senderLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
            messageBubble.getChildren().addAll(senderLabel, messageLabel); // Client's messages align left
            messageBubble.setStyle("-fx-alignment: CENTER_LEFT;");
        }

        chatArea.getChildren().add(messageBubble);
    }

    //back to homepage

    public void menu(ActionEvent e) {
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
}
