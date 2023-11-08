package com.uexcel.spring.security.controller;

import com.uexcel.spring.security.model.LoginModel;
import com.uexcel.spring.security.model.UserModel;
import com.uexcel.spring.security.service.LoginAndEditAllService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FullEditController {
    @Autowired
   LoginAndEditAllService loginAndEditAllService;

   @PostMapping("/login")
   public String login(){
       return null;
   }

    @PostMapping("/loginEdit")
    public String loginEdit(
            @RequestBody LoginModel loginModel,
             HttpServletRequest request
    ){
      return loginAndEditAllService.loginReset(
              loginModel, applicationUrl(request));
    }

    @PutMapping("/edit/{token}")
    public String EditAll(@RequestBody UserModel userModel,
            @PathVariable("token") String token){

        return loginAndEditAllService.editUser(userModel, token);
    }

    private String applicationUrl(HttpServletRequest request) {
        String url = "http://"+ request.getServerName()+
                ":"+ request.getServerPort()
                + request.getContextPath();
        return url;
    }
}
