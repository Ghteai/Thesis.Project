package com.example.flightmanagementproject.auth;

import com.example.flightmanagementproject.models.User;

public class AuthService {
    private static User loggedInUser;

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public static void logout() {
        loggedInUser = null;
    }

}
