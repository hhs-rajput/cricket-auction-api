package com.cricket.mpl.dto.request;

import lombok.Data;

@Data
public class PlayerBidTransactionRequestDto {

    private Integer playerBidId;
    private Integer auctionId;
    private Integer teamId;
    private Integer playerId;
    private Integer playerBasePrice;
    private Integer bidAmount;
    private Integer createdBy;
}