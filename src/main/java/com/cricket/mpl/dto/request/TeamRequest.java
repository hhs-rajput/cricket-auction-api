package com.cricket.mpl.dto.request;

import lombok.Data;

@Data
public class TeamRequest {
    private String teamName;
    private Integer teamSize;
    private Integer captionUserId;
}
