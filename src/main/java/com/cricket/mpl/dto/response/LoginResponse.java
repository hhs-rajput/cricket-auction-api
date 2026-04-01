package com.cricket.mpl.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String userRole;
    private String name;
    private String team;
    private Integer userId;
    private Integer teamId;
    private String teamStatus;
}
