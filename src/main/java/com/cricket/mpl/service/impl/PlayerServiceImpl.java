package com.cricket.mpl.service.impl;

import com.cricket.mpl.dto.request.PlayerRequest;
import com.cricket.mpl.dto.request.SellPlayerRequest;
import com.cricket.mpl.dto.response.PlayerResponseDto;
import com.cricket.mpl.entity.Player;
import com.cricket.mpl.entity.Team;
import com.cricket.mpl.mapper.PlayerMapper;
import com.cricket.mpl.repository.PlayerRepository;
import com.cricket.mpl.repository.TeamRepository;
import com.cricket.mpl.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {


    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerMapper playerMapper;


    public PlayerServiceImpl(PlayerRepository playerRepository, TeamRepository teamRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.playerMapper = playerMapper;
    }

    @Override
    public List<PlayerResponseDto> getAllPlayers() {
        List<Player> allPlayers = playerRepository.findAll();
        List<Team> allTeams = teamRepository.findAll();
        Map<Integer, String> teamIdNameMap = null;
        if (!allTeams.isEmpty()) {
            teamIdNameMap = allTeams.stream().collect(Collectors.toMap(Team::getId, Team::getTeamName));
        }
        return playerMapper.playerEntityToPlayerResponseDTOList(allPlayers,teamIdNameMap);
    }

    @Override
    public void addPlayer(PlayerRequest player) {
        playerRepository.save(playerMapper.playerRequestDTOToPlayer(player));
    }

    @Override
    public void addPlayer(PlayerRequest player, Integer userId) {
        Player entity = playerMapper.playerRequestDTOToPlayer(player);
        entity.setUserId(userId);
        playerRepository.save(entity);
    }

    @Override
    public List<PlayerResponseDto> getAllPlayersByTeam(Integer teamId) {
        return playerMapper.playerEntityToPlayerResponseDTOList(playerRepository.findByTeamId(teamId));

    }

    @Override
    public List<PlayerResponseDto> getAllPlayers(String category) {
        List<Player> allPlayers = playerRepository.findByPlayerCategory(category);
        List<Team> allTeams = teamRepository.findAll();
        Map<Integer, String> teamIdNameMap = null;
        if (!allTeams.isEmpty()) {
            teamIdNameMap = allTeams.stream().collect(Collectors.toMap(Team::getId, Team::getTeamName));
        }
        return playerMapper.playerEntityToPlayerResponseDTOList(allPlayers,teamIdNameMap);
    }

    @Override
    public String sellPlayer(SellPlayerRequest sellPlayerRequest) {

        return "Player is sold. ";
    }
}
