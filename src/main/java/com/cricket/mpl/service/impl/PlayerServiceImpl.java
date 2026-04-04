package com.cricket.mpl.service.impl;

import com.cricket.mpl.dto.request.PlayerRequest;
import com.cricket.mpl.dto.request.RetainApproveRequest;
import com.cricket.mpl.dto.request.RetainPlayerRequest;
import com.cricket.mpl.dto.request.SellPlayerRequest;
import com.cricket.mpl.dto.response.PlayerResponseDto;
import com.cricket.mpl.dto.response.PlayerSoldDto;
import com.cricket.mpl.dto.response.RetainRequestsResponseDto;
import com.cricket.mpl.entity.*;
import com.cricket.mpl.mapper.PlayerMapper;
import com.cricket.mpl.repository.*;
import com.cricket.mpl.service.PlayerService;
import jakarta.transaction.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {


    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerMapper playerMapper;
    private final PlayerBidRepository playerBidRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final AuctionTeamRepository auctionTeamRepository;
    private final PlayerRetentionRepository playerRetentionRepository;


    public PlayerServiceImpl(PlayerRepository playerRepository, TeamRepository teamRepository, PlayerMapper playerMapper, PlayerBidRepository playerBidRepository, SimpMessagingTemplate messagingTemplate, AuctionTeamRepository auctionTeamRepository, PlayerRetentionRepository playerRetentionRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.playerMapper = playerMapper;
        this.playerBidRepository = playerBidRepository;
        this.messagingTemplate = messagingTemplate;
        this.auctionTeamRepository = auctionTeamRepository;
        this.playerRetentionRepository = playerRetentionRepository;
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
        return playerMapper.playerEntityToPlayerResponseDTOList(playerRepository.findBySoldAndTeamId(Boolean.TRUE,teamId));

    }

    @Override
    public List<PlayerResponseDto> getAllPlayers(String category) {
        List<Player> allPlayers = playerRepository.findBySoldAndCaptionAndPlayerCategory(false,false,category);
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

    @Override
    @Transactional
    public void retainPlayer(RetainPlayerRequest retainPlayerRequest) {
        PlayerRetention byAuctionIdAndTeamId = playerRetentionRepository.findByAuctionIdAndTeamId(retainPlayerRequest.getAuctionId(), retainPlayerRequest.getTeamId());
        if(byAuctionIdAndTeamId!=null){
            throw new RuntimeException("You cannot retain more than one player for the same auction.");
        }
        PlayerRetention playerRetention = new PlayerRetention();
        playerRetention.setPlayerName(retainPlayerRequest.getPlayerName());
        playerRetention.setTeamId(retainPlayerRequest.getTeamId());
        playerRetention.setAuctionName(retainPlayerRequest.getAuctionName());
        playerRetention.setTeamName(retainPlayerRequest.getTeamName());
        playerRetention.setPlayerId(retainPlayerRequest.getPlayerId());
        playerRetention.setAuctionId(retainPlayerRequest.getAuctionId());
        playerRetention.setCreatedBy(retainPlayerRequest.getUserId());
        playerRetention.setUpdatedBy(retainPlayerRequest.getUserId());
        playerRetention.setStatus("REVIEW");
        playerRetentionRepository.save(playerRetention);
    }

    @Override
    public List<RetainRequestsResponseDto> getAllRetainRequests() {
        List<PlayerRetention> allPlayerRetentionRequests = playerRetentionRepository.findByStatus("REVIEW");
        return allPlayerRetentionRequests.stream().map(playerRetention -> RetainRequestsResponseDto.builder()
                        .retainRequestId(playerRetention.getRetentionId())
                        .playerName(playerRetention.getPlayerName())
                        .teamName(playerRetention.getTeamName())
                        .auctionName(playerRetention.getAuctionName())
                        .build()).toList();
    }

    @Override
    @Transactional
    public void retainApprove(RetainApproveRequest retainApproveRequest) {
        PlayerRetention retainedPlayer = playerRetentionRepository.findById(retainApproveRequest.getRetainRequestId()).get();
        retainedPlayer.setStatus("APPROVED");
        retainedPlayer.setRetainPrice(retainApproveRequest.getRetainPrice());
        retainedPlayer.setUpdatedBy(retainApproveRequest.getUserId());
        playerRetentionRepository.save(retainedPlayer);
        AuctionTeam auctionTeam = auctionTeamRepository.findByAuctionIdAndTeamId(retainedPlayer.getAuctionId(), retainedPlayer.getTeamId());
        auctionTeam.setRemainingPurse(auctionTeam.getRemainingPurse()-retainApproveRequest.getRetainPrice());
        auctionTeamRepository.save(auctionTeam);
        playerRepository.findById(retainedPlayer.getPlayerId()).ifPresent(player -> {
            player.setTeamId(retainedPlayer.getTeamId());
            player.setSold(Boolean.TRUE);
            player.setSoldPrice(retainApproveRequest.getRetainPrice());
            playerRepository.save(player);
        });
    }

    @Override
    public void retainReject(RetainApproveRequest retainApproveRequest) {
        playerRetentionRepository.findById(retainApproveRequest.getRetainRequestId()).ifPresent(playerRetention -> {
            playerRetention.setStatus("REJECTED");
            playerRetention.setUpdatedBy(retainApproveRequest.getUserId());
            playerRetention.setTeamId(null);
            playerRetentionRepository.save(playerRetention);
        });
    }
}
