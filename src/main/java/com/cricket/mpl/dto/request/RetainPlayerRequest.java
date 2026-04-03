package com.cricket.mpl.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RetainPlayerRequest {
    private Integer playerId;
    private Integer teamId;
}
