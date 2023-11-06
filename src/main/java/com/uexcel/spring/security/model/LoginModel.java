package com.uexcel.spring.security.model;

import lombok.Data;
import org.springframework.stereotype.Component;
@Data
@Component
public class LoginModel {
    private String email;
    private String password;

}
