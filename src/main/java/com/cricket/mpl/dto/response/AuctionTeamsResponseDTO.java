package com.cricket.mpl.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuctionTeamsResponseDTO {
    private Integer teamId;
    private Integer captionUserId;
    private String teamName;
    private Integer teamSize;
    private Integer purse;
}
