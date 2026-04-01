package com.cricket.mpl.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerBidResponse {
    private Integer playerBidId;
    private Integer auctionId;
    private Integer leadingTeamId;
    private String leadingTeamName;
    private Integer playerId;
    private Integer currentBidAmount;

}
