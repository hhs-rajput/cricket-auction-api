package com.cricket.mpl.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LiveAuctionCurrentPlayerResponseDTO {
    private Integer playerId;
    private String playerName;
    private String playerRole;
    private String playerCategory;
    private Integer basePrice;
    private Integer currentBid;
}
