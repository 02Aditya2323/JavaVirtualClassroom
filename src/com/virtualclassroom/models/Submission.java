package com.virtualclassroom.models;

public class Submission {
    private int id;
    private int assignmentId;
    private int studentId;
    private String content;
    private String submittedAt;

    // Additional fields for display
    private String studentName;
    private String assignmentTitle;

    // Constructor for creating new submission
    public Submission(int assignmentId, int studentId, String content, String submittedAt) {
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.content = content;
        this.submittedAt = submittedAt;
    }

    // Constructor for loading from database
    public Submission(int id, int assignmentId, int studentId, String content, String submittedAt) {
        this.id = id;
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.content = content;
        this.submittedAt = submittedAt;
    }

    // Getters
    public int getId() { return id; }
    public int getAssignmentId() { return assignmentId; }
    public int getStudentId() { return studentId; }
    public String getContent() { return content; }
    public String getSubmittedAt() { return submittedAt; }
    public String getStudentName() { return studentName; }
    public String getAssignmentTitle() { return assignmentTitle; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setAssignmentTitle(String assignmentTitle) { this.assignmentTitle = assignmentTitle; }

    @Override
    public String toString() {
        return "Assignment: " + assignmentTitle + " | Student: " + studentName + " | Submitted: " + submittedAt;
    }
}