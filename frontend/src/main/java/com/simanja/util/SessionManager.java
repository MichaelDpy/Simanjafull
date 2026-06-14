package com.simanja.util;

import com.simanja.model.User;

/**
 * SessionManager — Singleton untuk menyimpan state user yang sedang login
 * Demonstrasi Encapsulation (private constructor + static instance)
 */
public class SessionManager {

    private static SessionManager instance;
    private User currentUser;
    private String jwtToken;
    private boolean isLoggedIn = false;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(User user, String token) {
        this.currentUser = user;
        this.jwtToken = token;
        this.isLoggedIn = true;
    }

    public void logout() {
        this.currentUser = null;
        this.jwtToken = null;
        this.isLoggedIn = false;
    }

    public User getCurrentUser() { return currentUser; }
    public String getJwtToken() { return jwtToken; }
    public boolean isLoggedIn() { return isLoggedIn; }
}
