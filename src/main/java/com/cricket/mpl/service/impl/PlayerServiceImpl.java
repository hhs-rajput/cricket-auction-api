package com.cricket.mpl.service.impl;

import com.cricket.mpl.dto.request.PlayerRequest;
import com.cricket.mpl.dto.request.RetainApproveRequest;
import com.cricket.mpl.dto.request.RetainPlayerRequest;
import com.cricket.mpl.dto.request.SellPlayerRequest;
import com.cricket.mpl.dto.request.PlayerUpdateRequest;
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
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;


    public PlayerServiceImpl(PlayerRepository playerRepository, TeamRepository teamRepository, PlayerMapper playerMapper, PlayerBidRepository playerBidRepository, SimpMessagingTemplate messagingTemplate, AuctionTeamRepository auctionTeamRepository, PlayerRetentionRepository playerRetentionRepository, UserRepository userRepository, AuctionRepository auctionRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.playerMapper = playerMapper;
        this.playerBidRepository = playerBidRepository;
        this.messagingTemplate = messagingTemplate;
        this.auctionTeamRepository = auctionTeamRepository;
        this.playerRetentionRepository = playerRetentionRepository;
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
    }

    @Override
    public List<PlayerResponseDto> getAllPlayers() {
        List<Player> allPlayers = playerRepository.findByAdmin(Boolean.FALSE);
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
    @Transactional
    public void updatePlayer(PlayerUpdateRequest playerUpdateRequest) {
        Player player = playerRepository.findById(playerUpdateRequest.getPlayerId())
                .orElseThrow(() -> new RuntimeException("Player not found"));
        player.setPlayerName(playerUpdateRequest.getPlayerName());
        player.setPlayerRole(playerUpdateRequest.getPlayerRole());
        player.setPlayerCategory(playerUpdateRequest.getPlayerCategory());
        player.setBasePrice(playerUpdateRequest.getBasePrice());
        player.setCaption(playerUpdateRequest.getCaption());
        playerRepository.save(player);
        if(playerUpdateRequest.getCaption()){
            User user = userRepository.findById(player.getUserId()).get();
            user.setRole("CAPTION");
            userRepository.save(user);
        }
    }

    @Override
    public List<PlayerResponseDto> getAllPlayersByTeam(Integer teamId) {
        return playerMapper.playerEntityToPlayerResponseDTOList(playerRepository.findBySoldAndTeamId(Boolean.TRUE,teamId));

    }

    @Override
    public List<PlayerResponseDto> getAllPlayers(String category) {
        List<Player> allPlayers = playerRepository.findBySoldAndCaptionAndAdminAndPlayerCategory(false,false,false,category);
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
    public void deletePlayer(Integer playerId) {
        playerRepository.deleteById(playerId);
    }

    @Override
    @Transactional
    public void removePlayer(Integer playerId, Integer teamId) {
        Player player = playerRepository.findById(playerId).get();
        int soldPrice = player.getSoldPrice();
        player.setSold(false);
        player.setTeamId(null);
        player.setSoldPrice(0);
        playerRepository.save(player);
        AuctionTeam auctionTeam = auctionTeamRepository.findByTeamIdAndAuctionCompleted(teamId, Boolean.FALSE);
        auctionTeam.setRemainingPurse(auctionTeam.getRemainingPurse()+soldPrice);
        auctionTeamRepository.save(auctionTeam);
    }

    @Override
    @Transactional
    public PlayerSoldDto sellPlayer(Integer playerBidId) {
        PlayerBid playerBid = playerBidRepository.findById(playerBidId).get();

        playerBidRepository.findById(playerBidId);
        AuctionTeam auctionTeam = auctionTeamRepository.
                findByAuctionIdAndTeamId
                        (playerBid.getAuctionId(), playerBid.getLeadingTeamId());
        auctionTeam.setRemainingPurse(auctionTeam.getRemainingPurse()-playerBid.getBidAmount());
        auctionTeamRepository.save(auctionTeam);


        playerBid.setStatus("BID_CLOSED");
        playerBidRepository.save(playerBid);


        Player player = playerRepository.findById(playerBid.getPlayerId()).get();
        player.setSold(Boolean.TRUE);
        player.setTeamId(playerBid.getLeadingTeamId());
        player.setSoldPrice(playerBid.getBidAmount());
        playerRepository.save(player);
        PlayerSoldDto playerSoldDto = PlayerSoldDto.builder()
                .playerId(playerBid.getPlayerId())
                .playerName(player.getPlayerName())
                .leadingTeamName(auctionTeam.getTeamName())
                .leadingTeamId(auctionTeam.getTeamId()).build();
        messagingTemplate.convertAndSend("/topic/sell-player/"+playerBid.getAuctionId() ,
                playerSoldDto);
        return playerSoldDto;
    }

    @Override
    @Transactional
    public void retainPlayer(RetainPlayerRequest retainPlayerRequest) {

        Auction auction = auctionRepository.findById(retainPlayerRequest.getAuctionId()).get();
        if(auction.getStatus().equals("COMPLETED")) {
            throw new RuntimeException(auction.getAuctionName()+" is already completed, please register for different auction.");
        }
        PlayerRetention byAuctionIdAndTeamId = playerRetentionRepository.findByAuctionIdAndTeamIdAndStatus(retainPlayerRequest.getAuctionId(), retainPlayerRequest.getTeamId(),"REVIEW");
        if(byAuctionIdAndTeamId!=null && !byAuctionIdAndTeamId.getStatus().equalsIgnoreCase("rejected")){
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
        try {
            playerRetentionRepository.findById(retainApproveRequest.getRetainRequestId()).ifPresent(playerRetention -> {
                playerRetention.setStatus("REJECTED");
                playerRetention.setUpdatedBy(retainApproveRequest.getUserId());
                playerRetentionRepository.save(playerRetention);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
