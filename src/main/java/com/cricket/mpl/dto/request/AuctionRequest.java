package com.cricket.mpl.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuctionRequest {
    private String auctionName;
    private LocalDateTime auctionDate;
}
