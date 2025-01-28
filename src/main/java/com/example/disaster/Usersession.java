package com.example.disaster;

public class Usersession {
    private static Usersession instance;

    private String firstname;
    private String lastname;
    private String username;
    private String role;

    private Usersession() {}

    public static Usersession getInstance() {
        if (instance == null) {
            instance = new Usersession();
        }
        return instance;
    }

    public void setUserDetails(String firstname, String lastname, String username, String role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.role = role;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
