package com.example.disaster;

public class Task {
    private int id;
    private String title;
    private String description;
    private String location;
    private String disasterType;
    private String assignedDate;
    private String status;

    public Task(){};
    public Task(int id, String title, String description, String location, String disasterType, String assignedDate, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.disasterType = disasterType;
        this.assignedDate = assignedDate;
        this.status = status;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDisasterType() {
        return disasterType;
    }

    public void setDisasterType(String disasterType) {
        this.disasterType = disasterType;
    }

    public String getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(String assignedDate) {
        this.assignedDate = assignedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
