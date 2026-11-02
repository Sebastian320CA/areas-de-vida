package com.usta.ecogrow.ui;

import com.usta.ecogrow.model.User;

/** Singleton con el usuario autenticado. */
public final class Session {

    private static final Session INSTANCE = new Session();

    private User user;

    private Session() {}

    public static Session get() {
        return INSTANCE;
    }

    public User user() {
        return user;
    }

    public void start(User user) {
        this.user = user;
    }

    public void end() {
        this.user = null;
    }

    public boolean canWrite() {
        return user != null && user.role().canWrite();
    }

    public boolean isAdmin() {
        return user != null && user.role().canManageUsers();
    }
}
