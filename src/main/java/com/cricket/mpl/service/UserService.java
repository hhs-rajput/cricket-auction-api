package com.cricket.mpl.service;

import com.cricket.mpl.dto.request.UserRequest;
import com.cricket.mpl.dto.response.UserDetailsResponse;
import com.cricket.mpl.entity.User;

public interface UserService {
   User addUser(UserRequest userRequest);

    UserDetailsResponse getUserDetails(Integer userId);
}
