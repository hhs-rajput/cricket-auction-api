package com.cricket.mpl.service.impl;

import com.cricket.mpl.dto.request.TeamRequest;
import com.cricket.mpl.dto.response.MyTeamDetailsResponse;
import com.cricket.mpl.dto.response.PlayerResponseDto;
import com.cricket.mpl.dto.response.TeamAndAuctionResponseDTO;
import com.cricket.mpl.dto.response.TeamAndCaptionResponseDTO;
import com.cricket.mpl.entity.*;
import com.cricket.mpl.mapper.PlayerMapper;
import com.cricket.mpl.repository.*;
import com.cricket.mpl.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {


    private final TeamRepository teamRepository;
    private final AuctionRepository auctionRepository;
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final PlayerMapper playerMapper;
    private final AuctionTeamRepository auctionTeamRepository;

    public TeamServiceImpl(TeamRepository teamRepository, AuctionRepository auctionRepository, PlayerRepository playerRepository, UserRepository userRepository, PlayerMapper playerMapper, AuctionTeamRepository auctionTeamRepository) {
        this.teamRepository = teamRepository;
        this.auctionRepository = auctionRepository;
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
        this.playerMapper = playerMapper;
        this.auctionTeamRepository = auctionTeamRepository;
    }

    @Override
    public Team createTeam(TeamRequest request) {
        Team team=new Team();
               team.setTeamName(request.getTeamName());
               team.setTeamSize(request.getTeamSize());
               team.setCaptionUserId(request.getCaptionUserId());
               team.setTeamStatus("SUBMITTED");
        return teamRepository.save(team);
    }

    @Override
    public List<Team> findByTeamIds(List<Integer> ids) {
        return teamRepository.findByIdIn(ids);
    }

    @Override
    public Team findByCaptainId(Integer userId) {
        return teamRepository.findByCaptionUserId(userId);
    }

    @Override
    public String approveTeam(Integer teamId, Integer captionUserId) {
        Team team = teamRepository.findById(teamId).get();
        team.setTeamStatus("APPROVED");
        teamRepository.save(team);
        Player player = playerRepository.findByUserId(captionUserId);
        player.setTeamId(team.getId());
        player.setCaption(Boolean.TRUE);
        player.setPlayerCategory("A");
        player.setSold(Boolean.TRUE);
        player.setSoldPrice(player.getSoldPrice());
        playerRepository.save(player);

        User user = userRepository.findById(player.getUserId()).get();
        user.setRole("CAPTION");
        userRepository.save(user);

        return "Team approved successfully!";

    }

    @Override
    public List<TeamAndCaptionResponseDTO> teamRequests() {
        return getTeamDetailsByTeamStatus("SUBMITTED");
    }



    @Override
    public MyTeamDetailsResponse myTeamDetails(Integer captionUserId) {

            Team team = teamRepository.findByCaptionUserId(captionUserId);
        List<Player> teamPlayers = playerRepository.findBySoldAndTeamId(Boolean.TRUE,team.getId());
        String captionName = teamPlayers.stream().filter(Player::getCaption).findFirst().map(Player::getPlayerName).orElse("");
        List<PlayerResponseDto> players = playerMapper.playerEntityToPlayerResponseDTOList(teamPlayers);
        return MyTeamDetailsResponse.builder()
                .teamSize(team.getTeamSize())
                .captionName(captionName)
                .teamName(team.getTeamName())
                .players(players).build();
    }

    @Override
    public List<TeamAndCaptionResponseDTO> getAllTeams() {
        return getTeamDetailsByTeamStatus("APPROVED");
    }

    @Override
    public List<TeamAndAuctionResponseDTO> auctionMappings() {
        List<TeamAndAuctionResponseDTO> responseDTOS=new ArrayList<>();
        List<Auction> auctions = auctionRepository.findAll();
        Map<Integer, String> auctionsMap = auctions.stream().collect(Collectors.toMap(Auction::getAuctionId, Auction::getAuctionName));
        List<AuctionTeam> auctionTeams = auctionTeamRepository.findAll();
        for(AuctionTeam auctionTeam:auctionTeams){
            TeamAndAuctionResponseDTO teamAndAuctionResponseDTO=new TeamAndAuctionResponseDTO();
            teamAndAuctionResponseDTO.setAuctionTeamId(auctionTeam.getAuctionTeamId());
            teamAndAuctionResponseDTO.setTeamName(auctionTeam.getTeamName());
            teamAndAuctionResponseDTO.setTeamId(auctionTeam.getTeamId());
            teamAndAuctionResponseDTO.setAuctionId(auctionTeam.getAuctionId());
            teamAndAuctionResponseDTO.setAuctionName(auctionsMap.get(auctionTeam.getAuctionId()));
            teamAndAuctionResponseDTO.setRemainingPurse(auctionTeam.getRemainingPurse());
            teamAndAuctionResponseDTO.setAuctionStatus(auctionTeam.isAuctionCompleted()?"COMPLETED":"IN-PROGRESS");
            responseDTOS.add(teamAndAuctionResponseDTO);
        }
        return responseDTOS;
    }

    @Override
    public String deleteAuctionMapping(Integer auctionTeamId) {
        auctionTeamRepository.deleteById(auctionTeamId);
        return "Auction Team Deleted Successfully !";
    }

    @Override
    public String updateTeamAuctionRemainingPurse(Integer auctionTeamId, Integer newPurse) {
        AuctionTeam auctionTeam = auctionTeamRepository.findById(auctionTeamId).get();
        auctionTeam.setRemainingPurse(newPurse);
        auctionTeamRepository.save(auctionTeam);
        return "Purse updated successfully !";
    }

    private List<TeamAndCaptionResponseDTO> getTeamDetailsByTeamStatus(String teamStatus) {
        List<Team> submittedTeams = teamRepository.findByTeamStatus(teamStatus);
        List<Integer> captionUserIds = submittedTeams.stream().map(Team::getCaptionUserId).toList();
        List<User> captions = userRepository.findAllById(captionUserIds);
        Map<Integer, String> userIdAndNames = captions.stream().collect(Collectors.toMap(User::getUserId, User::getName));

        return submittedTeams.stream().map(team -> TeamAndCaptionResponseDTO.builder()
                .teamId(team.getId())
                .teamSize(team.getTeamSize())
                .teamName(team.getTeamName())
                .captionUserId(team.getCaptionUserId())
                .teamCaption(userIdAndNames.get(team.getCaptionUserId()))
                .build()).toList();
    }
}
