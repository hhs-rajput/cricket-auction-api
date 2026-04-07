package com.cricket.mpl.dto.request;

import lombok.Data;

@Data
public class PlayerUpdateRequest {
    private Integer playerId;
    private String playerName;
    private String playerRole;
    private String playerCategory;
    private Integer basePrice;
    private Boolean caption;
}
