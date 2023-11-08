package com.uexcel.spring.security.service;

import com.uexcel.spring.security.entity.User;
import com.uexcel.spring.security.model.UserModel;

public interface UserService {
    User registerUser(UserModel userModel);

    String saveTokens(User user);

    String verifyUserToken(String token);

    String reset(String email, String url, String servletPath);

    String resetUserPassword(
            String token , String password);
}
