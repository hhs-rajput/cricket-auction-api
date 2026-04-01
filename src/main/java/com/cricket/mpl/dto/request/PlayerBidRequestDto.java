package com.cricket.mpl.dto.request;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerBidRequestDto {

    private Integer auctionId;
    private Integer teamId;
    private Integer playerId;
    private Integer bidAmount;
}
