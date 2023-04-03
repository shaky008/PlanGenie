package com.example.plangenie;
//class to store full name and email of the user
public class User {

    private String fullName, email;

    //default constructor
    public User()
    {

    }

    //parameterized constructor
    public User (String newFullName, String newEmail)
    {
        this.fullName = newFullName;
        this.email = newEmail;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
