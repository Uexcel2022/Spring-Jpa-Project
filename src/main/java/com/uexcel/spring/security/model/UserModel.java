package com.uexcel.spring.security.model;

import lombok.Data;
import org.springframework.stereotype.Component;
@Component
@Data
public class UserModel {
    private String firstName;
    private String lastName;
    private String phone;
    private String  email;
    private String password;
    private String matchingPassword;
}
