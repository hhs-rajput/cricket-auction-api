package com.cricket.mpl.dto.request;

import lombok.Data;

@Data
public class RetainApproveRequest {
    private Integer teamId;
    private Integer userId;
    private Integer retainRequestId;
    private Integer playerId;
    private Integer retainPrice;
}
