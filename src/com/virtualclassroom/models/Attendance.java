package com.virtualclassroom.models;

public class Attendance {
    private int id;
    private int lectureId;
    private int studentId;
    private String status; // "Present" or "Absent"
    private String markedAt;

    // Additional fields for display
    private String studentName;
    private String lectureTitle;

    // Constructor for creating new attendance
    public Attendance(int lectureId, int studentId, String status, String markedAt) {
        this.lectureId = lectureId;
        this.studentId = studentId;
        this.status = status;
        this.markedAt = markedAt;
    }

    // Constructor for loading from database
    public Attendance(int id, int lectureId, int studentId, String status, String markedAt) {
        this.id = id;
        this.lectureId = lectureId;
        this.studentId = studentId;
        this.status = status;
        this.markedAt = markedAt;
    }

    // Getters
    public int getId() { return id; }
    public int getLectureId() { return lectureId; }
    public int getStudentId() { return studentId; }
    public String getStatus() { return status; }
    public String getMarkedAt() { return markedAt; }
    public String getStudentName() { return studentName; }
    public String getLectureTitle() { return lectureTitle; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setStatus(String status) { this.status = status; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setLectureTitle(String lectureTitle) { this.lectureTitle = lectureTitle; }

    @Override
    public String toString() {
        return "Lecture: " + lectureTitle + " | Student: " + studentName + " | Status: " + status;
    }
}