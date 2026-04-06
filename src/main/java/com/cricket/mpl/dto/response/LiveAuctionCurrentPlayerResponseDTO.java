package com.cricket.mpl.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LiveAuctionCurrentPlayerResponseDTO {
    private Integer playerBidId;
    private Integer auctionId;
    private Integer playerId;
    private String playerName;
    private String playerRole;
    private Boolean autoSale;
    private String playerCategory;
    private Integer basePrice;
    private Integer currentBidAmount;
    private Integer sellTime;
    private Integer leadingTeamId;
    private String leadingTeamName;
    private LocalDateTime createdAt;
}
