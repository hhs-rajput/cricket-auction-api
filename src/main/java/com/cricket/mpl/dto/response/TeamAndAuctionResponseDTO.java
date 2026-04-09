package com.cricket.mpl.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamAndAuctionResponseDTO {
    private Integer auctionTeamId;
    private Integer teamId;
    private String teamName;
    private Integer auctionId;
    private Integer remainingPurse;
    private String auctionName;
    private String auctionStatus;
}
