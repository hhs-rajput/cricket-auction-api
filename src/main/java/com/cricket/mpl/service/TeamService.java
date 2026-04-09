package com.cricket.mpl.service;

import com.cricket.mpl.dto.request.TeamRequest;
import com.cricket.mpl.dto.response.MyTeamDetailsResponse;
import com.cricket.mpl.dto.response.TeamAndAuctionResponseDTO;
import com.cricket.mpl.dto.response.TeamAndCaptionResponseDTO;
import com.cricket.mpl.entity.Team;

import java.util.List;

public interface TeamService {
   Team createTeam(TeamRequest request);

   List<Team> findByTeamIds(List<Integer> ids);

   Team findByCaptainId(Integer userId);

   String approveTeam(Integer teamId, Integer captionUserId);

   List<TeamAndCaptionResponseDTO> teamRequests();

   MyTeamDetailsResponse myTeamDetails(Integer captionUserId);

   List<TeamAndCaptionResponseDTO>  getAllTeams();

   List<TeamAndAuctionResponseDTO> auctionMappings();

   String deleteAuctionMapping(Integer auctionTeamId);

   String updateTeamAuctionRemainingPurse(Integer auctionTeamId, Integer newPurse);
}
