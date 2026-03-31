package com.cricket.mpl.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetailsResponse {
    private String userRole;
    private String name;
    private String team;
    private Integer userId;
    private String teamStatus;
}
