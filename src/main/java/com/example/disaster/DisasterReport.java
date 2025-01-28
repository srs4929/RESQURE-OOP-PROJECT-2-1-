package com.example.disaster;

public class DisasterReport {
    private int reportId;
    private String location;
    private String disasterType;
    private String severity;
    private String status;
    private String dateReported;

    public DisasterReport(int reportId, String location, String disasterType, String severity, String status, String dateReported) {
        this.reportId = reportId;
        this.location = location;
        this.disasterType = disasterType;
        this.severity = severity;
        this.status = status;
        this.dateReported = dateReported;
    }


    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
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

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateReported() {
        return dateReported;
    }

    public void setDateReported(String dateReported) {
        this.dateReported = dateReported;
    }
}
