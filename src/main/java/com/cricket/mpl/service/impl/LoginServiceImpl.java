package com.cricket.mpl.service.impl;

import com.cricket.mpl.dto.request.LoginRequest;
import com.cricket.mpl.dto.request.UserRequest;
import com.cricket.mpl.entity.User;
import com.cricket.mpl.repository.UserRepository;
import com.cricket.mpl.service.LoginService;
import com.cricket.mpl.service.UserService;
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
}
