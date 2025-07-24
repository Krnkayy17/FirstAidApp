package com.example.firstaidapp.models;

public class User {
    private int userId;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userType; // Two types, VADs or general public
    private String userImage;

    // Default constructor
    public User() {

    }

    // Constructor
    public User(int userId, String userName, String userEmail, String userPassword, String userType, String userImage) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userType = userType;
        this.userImage = userImage;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserType() { return userType; }

    public void setUserType(String userType) { this.userType = userType; }

    public String getUserImage() { return userImage; }

    public void setUserImage(String userImage) { this.userImage = userImage; }
}
