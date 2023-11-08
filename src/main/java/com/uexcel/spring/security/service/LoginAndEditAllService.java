package com.uexcel.spring.security.service;

import com.uexcel.spring.security.model.LoginModel;
import com.uexcel.spring.security.model.UserModel;

public interface LoginAndEditAllService {
    String loginReset(LoginModel loginModel, String string);

    String editUser(UserModel userModel, String token);
}
