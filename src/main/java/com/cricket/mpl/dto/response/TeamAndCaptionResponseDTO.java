package com.cricket.mpl.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamAndCaptionResponseDTO {
    private Integer teamId;
    private String teamName;
    private Integer teamSize;
    private String teamStatus;
    private String teamCaption;
    private Integer captionUserId;
}
