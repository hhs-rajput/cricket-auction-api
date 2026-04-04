package com.cricket.mpl.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerResponseDto {
    private Integer playerId;
    private String playerName;
    private Integer basePrice;
    private boolean sold;
    private int soldPrice;
    private String team;
    private String phoneNumber;
    private String playerCategory;
    private Boolean caption;

}
