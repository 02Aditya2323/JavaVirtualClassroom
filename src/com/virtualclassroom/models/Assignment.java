package com.virtualclassroom.models;

public class Assignment {
    private int id;
    private String title;
    private String description;
    private String dueDate; // Format: DD/MM/YYYY
    private int teacherId;
    private String teacherName;

    // Constructor for creating new assignment
    public Assignment(String title, String description, String dueDate, int teacherId) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.teacherId = teacherId;
    }

    // Constructor for loading from database
    public Assignment(int id, String title, String description, String dueDate, int teacherId, String teacherName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDueDate() { return dueDate; }
    public int getTeacherId() { return teacherId; }
    public String getTeacherName() { return teacherName; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    @Override
    public String toString() {
        return title + " (Due: " + dueDate + ")";
    }
}