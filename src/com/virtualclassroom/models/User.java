package com.virtualclassroom.models;

public class User {
    private int id;
    private String name;
    private String username;
    private String password;
    private String role; // "Teacher" or "Student"

    // Constructor for creating new user (no id yet)
    public User(String name, String username, String password, String role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Constructor for loading from database (with id)
    public User(int id, String name, String username, String password, String role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    // Setters
    public void setId(int id) { this.id = id; }

    @Override
    public String toString() {
        return name + " (" + role + ")";
    }
}