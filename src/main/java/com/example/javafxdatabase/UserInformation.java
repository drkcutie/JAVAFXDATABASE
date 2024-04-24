package com.example.javafxdatabase;

public class UserInformation {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private int userId;

    private static UserInformation instance;

    private UserInformation() {
    }

    public static UserInformation getInstance() {
        if (instance == null) {
            instance = new UserInformation();
        }
        return instance;
    }

    // Getters and setters for all fields

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
