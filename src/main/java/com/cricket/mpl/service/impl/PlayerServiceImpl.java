package com.cricket.mpl.service.impl;

import com.cricket.mpl.dto.request.PlayerRequest;
import com.cricket.mpl.dto.request.SellPlayerRequest;
import com.cricket.mpl.dto.response.PlayerResponseDto;
import com.cricket.mpl.dto.response.PlayerSoldDto;
import com.cricket.mpl.entity.AuctionTeam;
import com.cricket.mpl.entity.Player;
import com.cricket.mpl.entity.PlayerBid;
import com.cricket.mpl.entity.Team;
import com.cricket.mpl.mapper.PlayerMapper;
import com.cricket.mpl.repository.AuctionTeamRepository;
import com.cricket.mpl.repository.PlayerBidRepository;
import com.cricket.mpl.repository.PlayerRepository;
import com.cricket.mpl.repository.TeamRepository;
import com.cricket.mpl.service.PlayerService;
import jakarta.transaction.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {


    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerMapper playerMapper;
    private final PlayerBidRepository playerBidRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final AuctionTeamRepository auctionTeamRepository;


    public PlayerServiceImpl(PlayerRepository playerRepository, TeamRepository teamRepository, PlayerMapper playerMapper, PlayerBidRepository playerBidRepository, SimpMessagingTemplate messagingTemplate, AuctionTeamRepository auctionTeamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.playerMapper = playerMapper;
        this.playerBidRepository = playerBidRepository;
        this.messagingTemplate = messagingTemplate;
        this.auctionTeamRepository = auctionTeamRepository;
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
    @Transactional
    public PlayerSoldDto sellPlayer(SellPlayerRequest sellPlayerRequest) {

        AuctionTeam auctionTeam = auctionTeamRepository.
                findByAuctionIdAndTeamId
                        (sellPlayerRequest.getAuctionId(), sellPlayerRequest.getTeamId());
        auctionTeam.setRemainingPurse(auctionTeam.getRemainingPurse()-sellPlayerRequest.getSoldPrice());
        auctionTeamRepository.save(auctionTeam);
        PlayerBid playerBid = playerBidRepository.findByAuctionIdAndLeadingTeamIdAndStatus(sellPlayerRequest.getAuctionId(), sellPlayerRequest.getTeamId(), "BID_STARTED");
        playerBid.setStatus("BID_CLOSED");
        playerBidRepository.save(playerBid);
        Player player = playerRepository.findById(sellPlayerRequest.getPlayerId()).get();
        player.setSold(Boolean.TRUE);
        player.setTeamId(sellPlayerRequest.getTeamId());
        player.setSoldPrice(sellPlayerRequest.getSoldPrice());
        playerRepository.save(player);
        PlayerSoldDto playerSoldDto = PlayerSoldDto.builder()
                .playerId(playerBid.getPlayerId())
                .playerName(player.getPlayerName())
                .leadingTeamName(auctionTeam.getTeamName())
                .leadingTeamId(auctionTeam.getTeamId()).build();
        messagingTemplate.convertAndSend("/topic/sell-player/"+sellPlayerRequest.getAuctionId() ,
                playerSoldDto);
        return playerSoldDto;
    }
}
