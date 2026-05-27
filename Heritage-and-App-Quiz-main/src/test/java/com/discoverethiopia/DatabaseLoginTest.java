package com.discoverethiopia;

import com.discoverethiopia.dao.UserDAO;
import com.discoverethiopia.model.User;

public class DatabaseLoginTest {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        User admin = userDAO.login("admin", "admin123")
                .orElseThrow(() -> new IllegalStateException("Could not log in with admin/admin123."));

        System.out.println("Database connection works.");
        System.out.println("Logged in user: " + admin.getUsername());
        System.out.println("Role: " + admin.getRole().getDatabaseValue());
    }
}

