package com.uexcel.spring.security.service;

import com.uexcel.spring.security.entity.PasswordResetToken;
import com.uexcel.spring.security.entity.User;
import com.uexcel.spring.security.model.LoginModel;
import com.uexcel.spring.security.model.UserModel;
import com.uexcel.spring.security.repository.UserRepository;
import com.uexcel.spring.security.repository.PasswordResetTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class LonginAndEditAllImp implements LoginAndEditAllService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    public String loginReset(LoginModel loginModel, String applicationUrl) {
        String password = loginModel.getPassword();
        Optional<User> user = userRepository.findUserByEmail(loginModel.getEmail());
        if(user.isPresent()) {
            boolean isPasswordMatch =
                    passwordEncoder.matches(password, user.get().getPassword());
            if (isPasswordMatch) {
                Optional<PasswordResetToken> passwordResetToken =
                        passwordResetTokenRepository
                                .getPasswordTokenDetailsByUserId(user.get().getUserId());
                if (passwordResetToken.isEmpty()) {
                    return "We encountered error unable to fulfil your request";
                }
                PasswordResetToken oldToken = passwordResetToken.get();
                PasswordResetToken newToken =
                        new PasswordResetToken(UUID.randomUUID().toString());
                oldToken.setToken(newToken.getToken());
                oldToken.setTokenExpiryDate(newToken.getTokenExpiryDate());
                passwordResetTokenRepository.save(oldToken);
                String token = oldToken.getToken();
                String editUrl =
                        applicationUrl + "/edit/" + token;
                log.info("Use the link to reset your credentials {}", editUrl);
                return "Email Sent for validation";
            }
            return "Invalid log in credentials";
        }
        return "Invalid log in credentials";
    }

    @Override
    public String editUser(UserModel userModel, String token) {
        List<PasswordResetToken> passwordResetToken =
                passwordResetTokenRepository.findByToken(token);
        if(!passwordResetToken.isEmpty()){
            Calendar calendar = Calendar.getInstance();
            if(passwordResetToken.get(0).getTokenExpiryDate().getTime()
             - calendar.getTime().getTime() <= 0){
                return "The taken has expired";
            }
             User obj = passwordResetToken.get(0).getUser();
             User userObj = getUser(userModel, obj);
             userRepository.save(userObj);
             PasswordResetToken oldToken = passwordResetToken.get(0);
             PasswordResetToken resetToken =
                     new PasswordResetToken(UUID.randomUUID().toString());
             oldToken.setToken(resetToken.getToken());
             oldToken.setTokenExpiryDate(resetToken.getTokenExpiryDate());
             passwordResetTokenRepository.save(oldToken);
             return "Your credentials has been updated successfully.";

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
