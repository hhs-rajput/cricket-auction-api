package com.cricket.mpl.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionRegisterRequest {
    private Integer auctionId;
    private Integer teamId;
    private String teamName;
    private Integer captionUserId;
}
