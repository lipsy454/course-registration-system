package com.example.course.dto;

public class StudentRequest {

    private String name;
    private String emailId;
    private String courseName;

    // ================= GETTERS =================

    public String getName() {
        return name;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getCourseName() {
        return courseName;
    }

    // ================= SETTERS =================

    public void setName(String name) {
        this.name = name;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}