package com.cricket.mpl.service.impl;

import com.cricket.mpl.dto.request.PlayerBidRequest;
import com.cricket.mpl.dto.request.SellPlayerRequest;
import com.cricket.mpl.dto.response.LiveAuctionCurrentPlayerResponseDTO;
import com.cricket.mpl.dto.response.PlayerBidResponse;
import com.cricket.mpl.entity.AuctionTeam;
import com.cricket.mpl.entity.Player;
import com.cricket.mpl.entity.PlayerBid;
import com.cricket.mpl.entity.PlayerBidTransaction;
import com.cricket.mpl.repository.AuctionTeamRepository;
import com.cricket.mpl.repository.PlayerBidRepository;
import com.cricket.mpl.repository.PlayerBidTransactionRepository;
import com.cricket.mpl.repository.PlayerRepository;
import com.cricket.mpl.service.PlayerBidService;
import com.cricket.mpl.service.PlayerService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class PlayerBidServiceImpl implements PlayerBidService {

    private final PlayerBidRepository playerBidRepository;
    private final PlayerBidTransactionRepository playerBidTransactionRepository;
    private final PlayerRepository playerRepository;
    private final AuctionTeamRepository auctionTeamRepository;
    private final AuctionWebSocketService auctionWebSocketService;
    private final PlayerService playerService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public PlayerBidServiceImpl(PlayerBidRepository playerBidRepository, PlayerBidTransactionRepository playerBidTransactionRepository, PlayerRepository playerRepository, AuctionTeamRepository auctionTeamRepository, AuctionWebSocketService auctionWebSocketService, PlayerService playerService) {
        this.playerBidRepository = playerBidRepository;
        this.playerBidTransactionRepository = playerBidTransactionRepository;
        this.playerRepository = playerRepository;
        this.auctionTeamRepository = auctionTeamRepository;
        this.auctionWebSocketService = auctionWebSocketService;
        this.playerService = playerService;
    }

    @Override
    public void startBid(PlayerBidRequest playerBidRequest) {
        PlayerBid existingBid = playerBidRepository.findByAuctionIdAndStatus(playerBidRequest.getAuctionId(), "BID_STARTED");
        if (existingBid != null) {
            throw new RuntimeException("A bid is already in progress for this auction.");
        }
        PlayerBid playerBid=new PlayerBid();
        playerBid.setAuctionId(playerBidRequest.getAuctionId());
        playerBid.setPlayerId(playerBidRequest.getPlayerId());
        playerBid.setStatus("BID_STARTED");
        playerBid.setPlayerBasePrice(playerBidRequest.getBasePrice());
        playerBid.setAutoSale(playerBidRequest.getAutoSale());
        playerBid.setCreatedAt(LocalDateTime.now());
        playerBid.setUpdatedAt(LocalDateTime.now());
        playerBid.setCreatedBy(playerBidRequest.getUserId());
        playerBid.setLastUpdatedBy(playerBidRequest.getUserId());
        playerBidRepository.save(playerBid);

        if (playerBidRequest.getAutoSale()) {
            scheduler.schedule(() -> {
                Object o = playerService.sellPlayer(playerBid.getPlayerBidId());
            }, 30, TimeUnit.SECONDS);
        }
    }

    @Override
    public LiveAuctionCurrentPlayerResponseDTO getCurrentPlayer(Integer auctionId, Integer teamId) {
        PlayerBid playerBid = playerBidRepository.findByAuctionIdAndStatus(auctionId, "BID_STARTED");
        if (playerBid!=null) {
            AuctionTeam auctionTeam = auctionTeamRepository.findByAuctionIdAndTeamId(auctionId, playerBid.getLeadingTeamId());
            Player player = playerRepository.findById(playerBid.getPlayerId()).get();
            return getLiveAuctionCurrentPlayerResponseDTO(auctionId, playerBid, player,auctionTeam!=null?auctionTeam.getTeamName():null);
        }else{
            return null;
        }
    }

    private static LiveAuctionCurrentPlayerResponseDTO getLiveAuctionCurrentPlayerResponseDTO(Integer auctionId, PlayerBid playerBid, Player player, String leadingTeamName) {
        LiveAuctionCurrentPlayerResponseDTO responseDTO=new LiveAuctionCurrentPlayerResponseDTO();
        responseDTO.setAuctionId(auctionId);
        responseDTO.setCurrentBidAmount(playerBid.getBidAmount());
        responseDTO.setPlayerId(playerBid.getPlayerId());
        responseDTO.setPlayerBidId(playerBid.getPlayerBidId());
        responseDTO.setPlayerName(player.getPlayerName());
        responseDTO.setBasePrice(player.getBasePrice());
        responseDTO.setPlayerRole(player.getPlayerRole());
        responseDTO.setAutoSale(playerBid.getAutoSale());
        responseDTO.setLeadingTeamName(leadingTeamName);
        responseDTO.setLeadingTeamId(playerBid.getLeadingTeamId());
        responseDTO.setCreatedAt(playerBid.getCreatedAt());
        responseDTO.setPlayerCategory(player.getPlayerCategory());
        if (playerBid.getAutoSale()) {
            responseDTO.setSellTime(35);
        }
        return responseDTO;
    }

    @Override
    @Transactional
    public PlayerBidResponse updateBid(PlayerBidRequest playerBidRequest) {

        PlayerBid playerBid = playerBidRepository.findById(playerBidRequest.getPlayerBidId()).get();
        playerBid.setBidAmount(playerBidRequest.getBidAmount());
        playerBid.setLastUpdatedBy(playerBidRequest.getUserId());
        playerBid.setUpdatedAt(LocalDateTime.now());
        playerBid.setLeadingTeamId(playerBidRequest.getTeamId());
        PlayerBidTransaction playerBidTransaction = getPlayerBidTransaction(playerBidRequest);
        playerBidRepository.save(playerBid);
        playerBidTransactionRepository.save(playerBidTransaction);
        // do below if player is sold to update the remaining purse of the team
         AuctionTeam auctionTeam = auctionTeamRepository.findByAuctionIdAndTeamId(playerBidRequest.getAuctionId(), playerBidRequest.getTeamId());
        //auctionTeam.setRemainingPurse(auctionTeam.getRemainingPurse() - playerBidRequest.getBidAmount());
        //auctionTeamRepository.save(auctionTeam);

        PlayerBidResponse playerBidResponse = PlayerBidResponse.builder()
                .playerBidId(playerBid.getPlayerBidId())
                .playerId(playerBid.getPlayerId())
                .auctionId(playerBid.getAuctionId())
                .leadingTeamId(playerBid.getLeadingTeamId())
                .leadingTeamName(auctionTeam.getTeamName())
                .currentBidAmount(playerBid.getBidAmount())
                .build();
        auctionWebSocketService.sendBidUpdate(playerBidResponse);
        return playerBidResponse;


    }

    @Override
    public boolean isBiddingOn(Integer auctionId) {
        PlayerBid existingBid = playerBidRepository.findByAuctionIdAndStatus(auctionId, "BID_STARTED");
        return existingBid != null;
    }

    @Override
    public void cancelBid(Integer playerBidId) {
        PlayerBid playerBid = playerBidRepository.findById(playerBidId).get();
        playerBidRepository.deleteById(playerBidId);
        Integer playerId = playerBid.getPlayerId();
        Player player = playerRepository.findById(playerId).get();
        if(player.isSold()){
            player.setSold(false);
            player.setSoldPrice(0);
            player.setTeamId(null);
        }
        playerRepository.save(player);

    }

    private static PlayerBidTransaction getPlayerBidTransaction(PlayerBidRequest playerBidRequest) {
        PlayerBidTransaction playerBidTransaction=new PlayerBidTransaction();
        playerBidTransaction.setBidAmount(playerBidRequest.getBidAmount());
        playerBidTransaction.setAuctionId(playerBidRequest.getAuctionId());
        playerBidTransaction.setPlayerBidId(playerBidRequest.getPlayerBidId());
        playerBidTransaction.setPlayerBasePrice(playerBidRequest.getBasePrice());
        playerBidTransaction.setPlayerId(playerBidRequest.getPlayerId());
        playerBidTransaction.setTeamId(playerBidRequest.getTeamId());
        playerBidTransaction.setCreatedBy(playerBidRequest.getUserId());
        return playerBidTransaction;
    }
}
