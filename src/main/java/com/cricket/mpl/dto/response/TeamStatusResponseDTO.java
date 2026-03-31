package com.cricket.mpl.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamStatusResponseDTO {
    private Integer teamId;
    private String teamName;
    private String teamStatus;
}
