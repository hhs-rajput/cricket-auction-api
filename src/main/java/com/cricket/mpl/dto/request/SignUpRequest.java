package com.cricket.mpl.dto.request;

import lombok.Data;

@Data
public class SignUpRequest {
    private String name;
    private String mobile;
    private String password;
}
