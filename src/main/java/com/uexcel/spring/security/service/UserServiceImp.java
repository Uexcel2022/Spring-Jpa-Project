package com.uexcel.spring.security.service;

import com.uexcel.spring.security.entity.PasswordResetToken;
import com.uexcel.spring.security.entity.User;
import com.uexcel.spring.security.entity.VerificationToken;
import com.uexcel.spring.security.model.UserModel;
import com.uexcel.spring.security.repository.PasswordResetTokenRepository;
import com.uexcel.spring.security.repository.UserRepository;
import com.uexcel.spring.security.repository.VerificationTokenRepository;
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
    VerificationTokenRepository verificationTokenRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    PasswordResetTokenRepository resetPasswordRepository;

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
    public String saveTokens(User user) {
        VerificationToken verificationToken = new VerificationToken(user);
        PasswordResetToken passwordResetToken = new PasswordResetToken(user);
        verificationTokenRepository.save(verificationToken);
        resetPasswordRepository.save(passwordResetToken);
        return verificationToken.getToken();
    }

    @Override
    public String verifyUserToken(String token) {
        Optional<VerificationToken> userToken =
                verificationTokenRepository.findByToken(token);
        if (userToken.isPresent()) {
            Calendar calendar = Calendar.getInstance();
            if (userToken.get().getExpirationTime().getTime() -
                    calendar.getTime().getTime() <= 0) {
//                verificationTokenRepository.delete(userToken.get());
                return "Expired";
            }
            User user = userToken.get().getUser();
            user.setEnabled(true);
            userRepository.save(user);
            VerificationToken oldToken = userToken.get();
            VerificationToken restToken =
                    new VerificationToken(UUID.randomUUID().toString());
            oldToken.setToken(restToken.getToken());
            oldToken.setExpirationTime(restToken.getExpirationTime());
            verificationTokenRepository.save(oldToken);
            return "You have been verified successfully";
        }

        return "Bad user";
    }

    @Override
    public String reset(String email, String applicationUrl, String servletPath) {

        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isPresent()) {
            long userId = user.get().getUserId();
            if (servletPath.equals("/resendVerificationToken")) {
                Optional<VerificationToken> userToken =
                        verificationTokenRepository.getVerificationDetailsUserId(userId);
                if (userToken.isPresent()) {
                    String token = UUID.randomUUID().toString();
                    VerificationToken oldToken = userToken.get();
                    VerificationToken newToken = new VerificationToken(token);
                    oldToken.setToken(newToken.getToken());
                    oldToken.setExpirationTime(newToken.getExpirationTime());
                    verificationTokenRepository.save(oldToken);

                    String url = applicationUrl
                            + "/verifyRegistration?token=" + token;

                    log.info("Click of the on the link to verify your profile: {}", url);

                    return "Email has been sent";
                } else {
                    return "We encountered error; unable to fulfill your request";
                }
            }

            if (servletPath.equals("/resetPassword")) {
//                Long tokenId = resetPasswordRepository.findUserLastTokenId(userId);
                Optional<PasswordResetToken> passwordResetToken =
                        resetPasswordRepository.getPasswordTokenDetailsByUserId(userId);
//                        resetPasswordRepository.findById(tokenId);
                if (passwordResetToken.isPresent()) {
                    PasswordResetToken oldToken = passwordResetToken.get();
                    PasswordResetToken newToken =
                            new PasswordResetToken(UUID.randomUUID().toString());
                    oldToken.setToken(newToken.getToken());
                    oldToken.setTokenExpiryDate(newToken.getTokenExpiryDate());
                    resetPasswordRepository.save(oldToken);

                    String resetPasswordUrl =
                            applicationUrl + "/resetPassword/" + newToken.getToken();
                    log.info("Click on the link to reset password {}", resetPasswordUrl);

                    return "Verification email has been sent";
                }
            }

        }

            return "Invalid user email!!!";
    }


    @Override
    public String resetUserPassword (String token, String newPassword){
        List<PasswordResetToken> resetToken =
                resetPasswordRepository.findByToken(token);

        if (resetToken.isEmpty()) {
            return "Bad user";
        }

        Calendar calendar = Calendar.getInstance();
        if (
                resetToken.get(0)
                        .getTokenExpiryDate()
                        .getTime() - calendar.getTime().getTime()
                        <= 0
        ) {

            return "Expired...";
        }

        User user = resetToken.get(0).getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        PasswordResetToken oldToken = resetToken.get(0);
        PasswordResetToken tokenReset  =
                new PasswordResetToken(UUID.randomUUID().toString());
        oldToken.setToken(tokenReset.getToken());
        oldToken.setTokenExpiryDate(tokenReset.getTokenExpiryDate());
        resetPasswordRepository.save(oldToken);
        return "The password has been updated successfully.";
    }

}


