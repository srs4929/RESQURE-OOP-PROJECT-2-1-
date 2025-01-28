package com.example.disaster;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;



class User {
    String name;
    Socket socket;
    BufferedWriter writer;

    public User(String name, Socket socket, BufferedWriter writer) {
        this.name = name;
        this.socket = socket;
        this.writer = writer;
    }

    public String getName() {
        return name;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedWriter getWriter() {
        return writer;
    }
}

public class ChatServer {

    static int PORT = 6666; // Server port
    static  List<User> clients = new ArrayList<>(); // List of connected clients
    static User adminUser;






    static class ClientHandler implements Runnable {
        Socket socket;
        String clientName;
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                // First message from the client is the client's name
                clientName = reader.readLine();
                System.out.println("Client registered: " + clientName);

                // Check if this is the admin
                if (clientName.equalsIgnoreCase("Admin")) {
                    adminUser = new User(clientName, socket, writer);
                    System.out.println("Admin is connected.");
                } else {
                    synchronized (clients) {
                        clients.add(new User(clientName, socket, writer));
                    }


                    if (adminUser != null) {
                        adminUser.getWriter().write(clientName + " has connected.\n");
                        adminUser.getWriter().flush();
                    }
                }


                String message;
                while ((message = reader.readLine()) != null) {
                    if (clientName.equalsIgnoreCase("Admin")) {

                        String[] parts = message.split(":", 2);
                        if (parts.length == 2) {
                            String targetClient = parts[0].trim();
                            String adminMessage = parts[1].trim();
                            sendMessage(targetClient, "Admin: " + adminMessage);
                        }
                    } else {

                        if (adminUser != null) {
                            adminUser.getWriter().write(clientName + ": " + message + "\n");
                            adminUser.getWriter().flush();
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Connection with client " + clientName + " closed.");
            } finally {

                synchronized (clients) {
                    for (int i = clients.size() - 1; i >= 0; i--) {
                        if (clients.get(i).getName().equals(clientName)) {
                            clients.remove(i);
                        }
                    }
                }

                if (clientName.equalsIgnoreCase("Admin")) {
                    adminUser = null;
                    System.out.println("Admin disconnected.");
                }
            }
        }

        private void sendMessage(String clientName, String message) {
            synchronized (clients) {
                for (User client : clients) {
                    if (client.getName().equalsIgnoreCase(clientName)) {
                        try {
                            client.getWriter().write(message + "\n");
                            client.getWriter().flush();
                        } catch (IOException e) {
                            System.out.println("Failed to send message to " + clientName);
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
    }





    public static void main(String[] args) {
        System.out.println("Chat Server started...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());


                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error starting server: " + e.getMessage());
        }
    }



}