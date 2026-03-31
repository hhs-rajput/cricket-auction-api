package com.cricket.mpl.dto.request;

import com.cricket.mpl.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
public class UserRequest {
    private String name;
    private String role;
    private String password;
    private String username;

    public User toEntity() {
        User user = new User();
        user.setName(this.name);
        user.setRole(this.role);
        user.setPassword(this.password);
        user.setUsername(this.username);
        return user;
    }
}
