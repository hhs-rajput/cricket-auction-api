package com.cricket.mpl.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyTeamDetailsResponse {
    private String teamName;
    private Integer teamSize;
    private String captionName;
    private List<PlayerResponseDto> players;
}
