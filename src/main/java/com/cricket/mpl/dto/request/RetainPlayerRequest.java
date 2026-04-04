package com.cricket.mpl.dto.request;

import lombok.Data;

@Data
public class RetainPlayerRequest {
    private Integer playerId;
    private Integer teamId;
    private Integer auctionId;
    private String playerName;
    private String teamName;
    private String auctionName;
    private Integer userId;
}
