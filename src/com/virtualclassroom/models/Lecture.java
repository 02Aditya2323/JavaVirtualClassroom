package com.virtualclassroom.models;

public class Lecture {
    private int id;
    private String title;
    private String description;
    private String date; // Format: DD/MM/YYYY
    private String time; // Format: HH:MM
    private int teacherId;
    private String teacherName;

    // Constructor for creating new lecture
    public Lecture(String title, String description, String date, String time, int teacherId) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.teacherId = teacherId;
    }

    // Constructor for loading from database
    public Lecture(int id, String title, String description, String date, String time, int teacherId, String teacherName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public int getTeacherId() { return teacherId; }
    public String getTeacherName() { return teacherName; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    @Override
    public String toString() {
        return title + " - " + date + " at " + time;
    }
}