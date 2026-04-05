package com.cricket.mpl.dto.request;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private String mobile;
    private String password;
    private String confirmPassword;
}
