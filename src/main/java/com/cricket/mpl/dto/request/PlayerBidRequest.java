package com.cricket.mpl.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerBidRequest {
    private Integer auctionId;
    private Integer teamId;
    private Integer playerId;
    private Integer bidAmount;
    private Integer captionUserId;
}
