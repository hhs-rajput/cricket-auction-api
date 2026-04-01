package com.cricket.mpl.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LiveAuctionCurrentPlayerResponseDTO {
    private Integer playerBidId;
    private Integer playerId;
    private String playerName;
    private String playerRole;
    private String playerCategory;
    private Integer basePrice;
    private Integer currentBid;
}
