package com.cricket.mpl.dto.response;

import lombok.Data;

@Data
public class PlayerCategoryResponseDto {
    private Integer playerId;
    private String playerName;
    private Integer basePrice;
    private String playerCategory;
}
