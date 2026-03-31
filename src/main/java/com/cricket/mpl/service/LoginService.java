package com.cricket.mpl.service;

import com.cricket.mpl.dto.request.LoginRequest;
import com.cricket.mpl.dto.request.UserRequest;
import com.cricket.mpl.entity.User;

public interface LoginService {
   User login(LoginRequest loginRequest);
}
