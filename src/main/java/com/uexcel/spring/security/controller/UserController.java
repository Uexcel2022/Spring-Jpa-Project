package com.uexcel.spring.security.controller;

import com.uexcel.spring.security.entity.User;
import com.uexcel.spring.security.event.RegistrationCompleteEvent;
import com.uexcel.spring.security.model.UserModel;
import com.uexcel.spring.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @GetMapping("/")
    public String home(){
        return "I'm practicing Spring Security";
    }
    @PostMapping("/register")
    public  String saveUser(@RequestBody UserModel userModel,
                            HttpServletRequest request){
        if(userModel.getMatchingPassword()
                .equals(userModel.getPassword())){
          User user = userService.registerUser(userModel);
          applicationEventPublisher.publishEvent(
                  new RegistrationCompleteEvent(
                          user,applicationUrl(request))
          );
          return "Success";
        }
        return "passwords did not match";
    }

    private String applicationUrl(HttpServletRequest request) {
        String url = "http://"+ request.getServerName()+
                ":"+ request.getServerPort()
                + request.getContextPath();
        return url;
    }
}
