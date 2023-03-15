package com.example.plangenie;

public class User {

    public String fullName, email;

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


}
