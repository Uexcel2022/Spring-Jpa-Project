package com.uexcel.spring.security.service;

import com.uexcel.spring.security.entity.User;
import com.uexcel.spring.security.model.UserModel;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveUserToken(User user, String token);

    boolean verifyUserToken(String token);

    String findUserByEmail(String email, String url, String servletPath);

    String resetUserPassword(
            String token , String password);
}
