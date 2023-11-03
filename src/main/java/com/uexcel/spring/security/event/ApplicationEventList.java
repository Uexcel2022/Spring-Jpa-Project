package com.uexcel.spring.security.event;

import com.uexcel.spring.security.entity.User;
import com.uexcel.spring.security.entity.UserToken;
import com.uexcel.spring.security.repository.UserTokenRepository;
import com.uexcel.spring.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class ApplicationEventList implements
        ApplicationListener<RegistrationCompleteEvent> {
    @Autowired
    UserService userService;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        String token = UUID.randomUUID().toString();
        User user = event.getUser();
        userService.saveUserToken(user, token);
    }


}
