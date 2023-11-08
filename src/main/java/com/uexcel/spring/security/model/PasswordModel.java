package com.uexcel.spring.security.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PasswordModel {
    private String email;
    private String password;
}
