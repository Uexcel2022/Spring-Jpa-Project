package com.uexcel.spring.security.service;

import com.uexcel.spring.security.entity.ValidationToken;
import com.uexcel.spring.security.entity.User;
import com.uexcel.spring.security.model.LoginModel;
import com.uexcel.spring.security.model.UserModel;
import com.uexcel.spring.security.repository.UserRepository;
import com.uexcel.spring.security.repository.ValidationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LonginAndEditAllImp implements LoginAndEditAllService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ValidationTokenRepository validationTokenRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    public String login(LoginModel loginModel, String applicationUrl) {
        List<User> user =
                userRepository.findByEmailAndPassword(
                        loginModel.getEmail(),
                        passwordEncoder.encode(loginModel.getPassword())
                );

        if(!user.isEmpty()){
            ValidationToken validationToken =
                    new ValidationToken(loginModel.getEmail());
            validationTokenRepository.save(validationToken);
            String token = validationToken.getResetToken();
            String editUrl =
                    applicationUrl + "/edit/" + token;
            log.info("Use the link to reset your credentials {}", editUrl);
            return "Email Sent for validation";
        }
        return "Invalid log in credentials";
    }

    @Override
    public String editUser(UserModel userModel, String token) {
        List<ValidationToken> validationToken =
                validationTokenRepository.findByResetToken(token);
        if(!validationToken.isEmpty()){
            Calendar calendar = Calendar.getInstance();
            if(validationToken.get(0).generateExpiryTime().getTime()
             - calendar.getTime().getTime() <= 0){
                validationTokenRepository.delete(validationToken.get(0));
                return "The taken has expired";
            }

            Optional<User> user =
                    userRepository.findUserByEmail(validationToken.get(0).getEmail());
            if(user.isPresent()){
                User obj = user.get();
                User userObj = getUser(userModel, obj);
                userRepository.save(userObj);
                return "Your credentials has been updated successfully.";
            }
            return "We encountered error; unable to fulfil your request";

        }

        return "Bad user";
    }

    @NotNull
    private static User getUser(UserModel userModel, User userObj) {

        if(userModel.getFirstName() != null){
            userObj.setFirstName(userModel.getFirstName());
        }

        if(userModel.getLastName()!= null){
            userObj.setLastName(userModel.getLastName());
        }

        if(userModel.getPhone() != null){
            userObj.setPhone(userModel.getPhone());
        }

        if(userModel.getEmail() != null){
            userObj.setEmail(userModel.getEmail());
        }
        return userObj;
    }
}
