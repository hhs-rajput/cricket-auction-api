package com.cricket.mpl.dto.request;

import lombok.Data;

@Data
public class PlayerRequest {
    private String playerName;
    private int basePrice;
    private boolean sold;
    private int soldPrice;
    private Integer team;
    private String phoneNumber;
    private String playerCategory;
    private Integer userId;
}
