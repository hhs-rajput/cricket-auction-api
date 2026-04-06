package com.cricket.mpl.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellPlayerRequest {
    private Integer teamId;
    private Integer auctionId;
    private Integer playerId;
    private Integer soldPrice;
}
