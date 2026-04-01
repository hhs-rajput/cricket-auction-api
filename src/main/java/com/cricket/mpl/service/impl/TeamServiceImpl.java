package com.cricket.mpl.service.impl;

import com.cricket.mpl.dto.request.TeamRequest;
import com.cricket.mpl.dto.response.MyTeamDetailsResponse;
import com.cricket.mpl.dto.response.PlayerResponseDto;
import com.cricket.mpl.dto.response.TeamAndCaptionResponseDTO;
import com.cricket.mpl.entity.Player;
import com.cricket.mpl.entity.Team;
import com.cricket.mpl.entity.User;
import com.cricket.mpl.mapper.PlayerMapper;
import com.cricket.mpl.repository.PlayerRepository;
import com.cricket.mpl.repository.TeamRepository;
import com.cricket.mpl.repository.UserRepository;
import com.cricket.mpl.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {


    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final PlayerMapper playerMapper;

    public TeamServiceImpl(TeamRepository teamRepository, PlayerRepository playerRepository, UserRepository userRepository, PlayerMapper playerMapper) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
        this.playerMapper = playerMapper;
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
        playerRepository.save(player);
        return "Team approved successfully!";

    }

    @Override
    public List<TeamAndCaptionResponseDTO> teamRequests() {
        return getTeamDetailsByTeamStatus("SUBMITTED");
    }



    @Override
    public MyTeamDetailsResponse myTeamDetails(Integer captionUserId) {

            Team team = teamRepository.findByCaptionUserId(captionUserId);
        List<Player> teamPlayers = playerRepository.findByTeamId(team.getId());
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
