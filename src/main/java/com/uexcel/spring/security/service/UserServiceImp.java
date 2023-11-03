package com.uexcel.spring.security.service;

import com.uexcel.spring.security.entity.User;
import com.uexcel.spring.security.entity.UserToken;
import com.uexcel.spring.security.model.UserModel;
import com.uexcel.spring.security.repository.UserRepository;
import com.uexcel.spring.security.repository.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        userTokenRepository.save(new UserToken(user,token));
    }
}
