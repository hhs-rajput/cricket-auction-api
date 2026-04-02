package com.cricket.mpl.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerSoldDto {
    private Integer playerId;
    private Integer auctionId;
    private String playerName;
    private Integer soldPrice;
    private Integer leadingTeamId;
    private String leadingTeamName;
}
