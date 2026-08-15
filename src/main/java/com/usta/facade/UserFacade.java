package com.usta.facade;

import com.usta.models.user.User;
import com.usta.models.user.UserImplDAO;

public class UserFacade {
    private UserImplDAO userDao = new UserImplDAO();

    public User loginUser(String email, String password) throws Exception {
        try {
            return userDao.authenticate(email, password);
        } catch (Exception e) {
            throw new Exception("Error during login: " + e.getMessage(), e);
        }
    }

    public void registerUser(User user) throws Exception {
        try {
            userDao.add(user);
        } catch (Exception e) {
            throw new Exception("Error during registration: " + e.getMessage(), e);
        }
    }
}
