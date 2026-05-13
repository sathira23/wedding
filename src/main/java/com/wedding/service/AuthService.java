package com.wedding.service;

import com.wedding.model.User;
import com.wedding.model.UserRole;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AuthService {
    private static final Map<String, Credentials> USER_STORE = new HashMap<>();

    static {
        USER_STORE.put("admin", new Credentials("admin", "admin", "Admin User", UserRole.ADMIN));
        USER_STORE.put("organizer1", new Credentials("organizer1", "organizer1", "Olivia Organizer", UserRole.ORGANIZER));
        USER_STORE.put("organizer2", new Credentials("organizer2", "organizer2", "Noah Organizer", UserRole.ORGANIZER));
    }

    public Optional<User> authenticate(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }
        Credentials saved = USER_STORE.get(username.trim().toLowerCase());
        if (saved != null && saved.password.equals(password)) {
            return Optional.of(new User(saved.id, username.trim().toLowerCase(), saved.fullName, saved.role));
        }
        return Optional.empty();
    }

    private static class Credentials {
        private final int id;
        private final String password;
        private final String fullName;
        private final UserRole role;

        Credentials(String username, String password, String fullName, UserRole role) {
            this.id = username.hashCode();
            this.password = password;
            this.fullName = fullName;
            this.role = role;
        }
    }
}
