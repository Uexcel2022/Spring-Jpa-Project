package com.uexcel.spring.security.service;

import com.uexcel.spring.security.entity.ValidationToken;
import com.uexcel.spring.security.entity.User;
import com.uexcel.spring.security.entity.UserToken;
import com.uexcel.spring.security.model.UserModel;
import com.uexcel.spring.security.repository.ValidationTokenRepository;
import com.uexcel.spring.security.repository.UserRepository;
import com.uexcel.spring.security.repository.UserTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImp implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserTokenRepository userTokenRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ValidationTokenRepository resetPasswordRepository;

    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setEmail(userModel.getEmail());

        user.setPassword(passwordEncoder.encode(
                userModel.getPassword())
        );
        user.setPhone(userModel.getPhone());
        user.setRole("USER");
        userRepository.save(user);
        return user;
    }

    @Override
    public void saveUserToken(User user, String token) {
        UserToken userToken = new UserToken(user, token);
        userTokenRepository.save(userToken);
    }

    @Override
    public boolean verifyUserToken(String token) {
        Optional<UserToken> userToken = userTokenRepository.findByToken(token);
        if (userToken.isPresent()) {
            Calendar calendar = Calendar.getInstance();
            if (userToken.get().getExpirationTime().getTime() -
                    calendar.getTime().getTime() <= 0) {
                userTokenRepository.delete(userToken.get());
                return false;
            }
            User user = userToken.get().getUser();
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public String reset(String email, String applicationUrl, String servletPath) {

        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isPresent()) {
            if (servletPath.equals("/resendVerificationToken")) {
                long userId = user.get().getUserId();
                String token = UUID.randomUUID().toString();
                Date date = expiryTime();
                Long tokenId = userTokenRepository.findUserLastTokenId(userId);
                Optional<UserToken> userToken =
                        userTokenRepository.findById(tokenId);
                if (userToken.isPresent()) {
                    UserToken obj = userToken.get();
                    obj.setToken(token);
                    obj.setExpirationTime(date);
                    userTokenRepository.save(obj);

                    String url = applicationUrl
                            + "/verifyRegistration?token=" + token;

                    log.info("Click of the on the link to verify your profile: {}", url);

                    return "Email has been sent";
                } else {
                    return "We encountered error; unable to fulfill your request";
                }
            }

            if (servletPath.equals("/resetPassword")) {
                ValidationToken resetPassword = new ValidationToken(email);
                String token = resetPassword.getResetToken();
                resetPasswordRepository.save(resetPassword);
                String resetPasswordUrl =
                        applicationUrl + "/resetPassword/" + token;
                log.info("Click on the link to reset password {}", resetPasswordUrl);
                return "Email has been sent";
            }

            if (servletPath.equals("/resendPasswordResetToken")) {
                String token = UUID.randomUUID().toString();
                Date date = expiryTime();
                Long resetTokenId =
                        resetPasswordRepository.findUserLastTokenId(user.get().getEmail());
                Optional<ValidationToken> resetPassword =
                        resetPasswordRepository.findById(resetTokenId);
                if (resetPassword.isPresent()) {
                    ValidationToken obj = resetPassword.get();
                    obj.setTokenExpiryDate(date);
                    obj.setResetToken(token);
                    resetPasswordRepository.save(obj);
                    String resetPasswordUrl =
                            applicationUrl + "/resetPassword/" + token;
                    log.info("Click on the link to reset password {}", resetPasswordUrl);
                    return "Email has been sent";

                }

            }

        }
        return "Invalid user email!!!";
    }


        @Override
        public String resetUserPassword (String token, String password){
            List<ValidationToken> resetPassword =
                    resetPasswordRepository.findByResetToken(token);

            if (resetPassword.isEmpty()) {
                return "Bad user";
            }

            Calendar calendar = Calendar.getInstance();
            if (
                    resetPassword.get(0)
                            .getTokenExpiryDate()
                            .getTime() - calendar.getTime().getTime()
                            <= 0
            ) {
                resetPasswordRepository.delete(resetPassword.get(0));

                return "The has expired...";
            }


            int result = userRepository.updateUserByEmail(
                    resetPassword.get(0).getEmail(),
                    passwordEncoder.encode(password));
            resetPasswordRepository.delete(resetPassword.get(0));
            return "The password has been rest successfully.";
        }


        private Date expiryTime () {
            int expiresInTenMinutes = 10;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(new Date().getTime());
            calendar.add(Calendar.MINUTE, expiresInTenMinutes);

            return new Date(calendar.getTime().getTime());
        }

}
