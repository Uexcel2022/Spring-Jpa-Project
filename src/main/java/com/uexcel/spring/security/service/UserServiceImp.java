package com.uexcel.spring.security.service;

import com.uexcel.spring.security.entity.User;
import com.uexcel.spring.security.entity.UserToken;
import com.uexcel.spring.security.model.UserModel;
import com.uexcel.spring.security.repository.UserRepository;
import com.uexcel.spring.security.repository.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserTokenRepository userTokenRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
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
        UserToken userToken = new UserToken(user,token);
        userTokenRepository.save(userToken);
    }

    @Override
    public boolean verifyUserToken(String token) {
        UserToken userToken = userTokenRepository.findByToken(token);
        if(userToken == null){
            return false;
        }

        Calendar calendar = Calendar.getInstance();
        if(userToken.getExpirationTime().getTime() -
                calendar.getTime().getTime() <= 0 ){
            userTokenRepository.delete(userToken);
            return false;

        }

        User user = userToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        return true;
    }
}
