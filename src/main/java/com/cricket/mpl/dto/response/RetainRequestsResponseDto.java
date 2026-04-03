package com.cricket.mpl.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RetainRequestsResponseDto {
    private Integer retainRequestId;
    private String retainRequestStatus;
    private Integer teamId;
    private String teamName;
    private Integer playerId;
    private String playerName;

}
