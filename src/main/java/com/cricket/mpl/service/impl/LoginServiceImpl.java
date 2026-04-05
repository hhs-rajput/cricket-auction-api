package com.cricket.mpl.service.impl;

import com.cricket.mpl.dto.request.LoginRequest;
import com.cricket.mpl.dto.request.UpdatePasswordRequest;
import com.cricket.mpl.entity.User;
import com.cricket.mpl.repository.UserRepository;
import com.cricket.mpl.service.LoginService;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {


    private final UserRepository userRepository;

    public LoginServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(LoginRequest loginRequest) {
        return userRepository.findByUsernameAndPassword(loginRequest.getMobile(), loginRequest.getPassword());
    }

    @Override
    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        User user = userRepository.findByUsername(updatePasswordRequest.getMobile());
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (!updatePasswordRequest.getPassword().equals(updatePasswordRequest.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }
        user.setPassword(updatePasswordRequest.getPassword());
        userRepository.save(user);
    }
}
