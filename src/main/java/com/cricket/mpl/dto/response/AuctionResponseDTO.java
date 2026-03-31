package com.cricket.mpl.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuctionResponseDTO {
    private Integer auctionId;
    private boolean isActive;
    private String auctionName;
    private LocalDateTime auctionDate;
    private String status;
}
