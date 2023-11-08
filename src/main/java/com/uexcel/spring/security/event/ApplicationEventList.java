package com.uexcel.spring.security.event;

import com.uexcel.spring.security.entity.User;
import com.uexcel.spring.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
@Component
@Slf4j
public class ApplicationEventList implements
        ApplicationListener<RegistrationCompleteEvent> {
    @Autowired
    UserService userService;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = userService.saveTokens(user);
        String url =
                event.getApplicationUlr()+"/verifyRegistration?token="+ token;

        log.info(
                "Click of the on the link to verify your profile: {}",url
        );
    }

}
