package com.cricket.mpl.service.impl;

import com.cricket.mpl.dto.request.AutoSellSettingsUpdateRequest;
import com.cricket.mpl.dto.request.PlayerBidRequest;
import com.cricket.mpl.dto.response.LiveAuctionCurrentPlayerResponseDTO;
import com.cricket.mpl.dto.response.PlayerBidResponse;
import com.cricket.mpl.entity.*;
import com.cricket.mpl.repository.*;
import com.cricket.mpl.service.PlayerBidService;
import com.cricket.mpl.service.PlayerService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class PlayerBidServiceImpl implements PlayerBidService {


    private final PlayerBidRepository playerBidRepository;
    private final PlayerBidTransactionRepository playerBidTransactionRepository;
    private final PlayerRepository playerRepository;
    private final AuctionTeamRepository auctionTeamRepository;
    private final AuctionWebSocketService auctionWebSocketService;
    private final PlayerService playerService;
    private final AutoSellSettingsRepository autoSellSettingsRepository;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public PlayerBidServiceImpl(PlayerBidRepository playerBidRepository, PlayerBidTransactionRepository playerBidTransactionRepository, PlayerRepository playerRepository, AuctionTeamRepository auctionTeamRepository, AuctionWebSocketService auctionWebSocketService, PlayerService playerService, AutoSellSettingsRepository autoSellSettingsRepository) {
        this.playerBidRepository = playerBidRepository;
        this.playerBidTransactionRepository = playerBidTransactionRepository;
        this.playerRepository = playerRepository;
        this.auctionTeamRepository = auctionTeamRepository;
        this.auctionWebSocketService = auctionWebSocketService;
        this.playerService = playerService;
        this.autoSellSettingsRepository = autoSellSettingsRepository;
    }

    @Override
    public void autoSellSettings(AutoSellSettingsUpdateRequest autoSellSettingsUpdateRequest) {
        List<AutoSaleSettings> autoSaleSettings = autoSellSettingsRepository.findAll();
        for(AutoSaleSettings a : autoSaleSettings){
            switch (a.getPlayerCategory()) {
                case "A" -> a.setSeconds(autoSellSettingsUpdateRequest.getA());
                case "B" -> a.setSeconds(autoSellSettingsUpdateRequest.getB());
                case "C" -> a.setSeconds(autoSellSettingsUpdateRequest.getC());
                case "D" -> a.setSeconds(autoSellSettingsUpdateRequest.getD());
                default -> a.setSeconds(1000);
                }
        }
        autoSellSettingsRepository.saveAll(autoSaleSettings);
    }

    @Override
    public void startBid(PlayerBidRequest playerBidRequest) {
        PlayerBid existingBid = playerBidRepository.findByAuctionIdAndStatus(playerBidRequest.getAuctionId(), "BID_STARTED");
        if (existingBid != null) {
            throw new RuntimeException("A bid is already in progress for this auction.");
        }
        Player player = playerRepository.findById(playerBidRequest.getPlayerId()).get();
        PlayerBid playerBid=new PlayerBid();
        playerBid.setAuctionId(playerBidRequest.getAuctionId());
        playerBid.setPlayerId(playerBidRequest.getPlayerId());
        playerBid.setStatus("BID_STARTED");
        playerBid.setPlayerBasePrice(playerBidRequest.getBasePrice());
        playerBid.setAutoSale(playerBidRequest.getAutoSale());
        playerBid.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
        playerBid.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
        playerBid.setCreatedBy(playerBidRequest.getUserId());
        playerBid.setLastUpdatedBy(playerBidRequest.getUserId());
        playerBid.setAutoSellTimeInSeconds(getAutoSellTimerSeconds(player.getPlayerCategory()));
        playerBidRepository.save(playerBid);

        if (playerBidRequest.getAutoSale()) {
            scheduler.schedule(() -> {
                Object o = playerService.sellPlayer(playerBid.getPlayerBidId());
            }, playerBid.getAutoSellTimeInSeconds(), TimeUnit.SECONDS);
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
        responseDTO.setSellTime(playerBid.getAutoSellTimeInSeconds());
        return responseDTO;
    }

    private int getAutoSellTimerSeconds(String category) {
        List<AutoSaleSettings> autoSaleSettings = autoSellSettingsRepository.findAll();
        Map<String, Integer> map = autoSaleSettings.stream().collect(Collectors.toMap(AutoSaleSettings::getPlayerCategory, AutoSaleSettings::getSeconds));
        return switch (category) {
            case "A" -> map.get("A");
            case "B" -> map.get("B");
            case "C" -> map.get("C");
            case "D" -> map.get("D");
            default -> throw new IllegalArgumentException("Invalid category: " + category);
        };
    }

    @Override
    @Transactional
    public PlayerBidResponse updateBid(PlayerBidRequest playerBidRequest) {

        PlayerBid playerBid = playerBidRepository.findById(playerBidRequest.getPlayerBidId()).get();
        playerBid.setBidAmount(playerBidRequest.getBidAmount());
        playerBid.setLastUpdatedBy(playerBidRequest.getUserId());
        playerBid.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
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
            int soldPrice = player.getSoldPrice();
            Integer playerTeamId = player.getTeamId();
            player.setSold(false);
            player.setSoldPrice(0);
            player.setTeamId(null);
            playerRepository.save(player);
            AuctionTeam auctionTeam = auctionTeamRepository.findByAuctionIdAndTeamId(playerBid.getAuctionId(), playerTeamId);
            auctionTeam.setRemainingPurse(auctionTeam.getRemainingPurse()+soldPrice);
            auctionTeamRepository.save(auctionTeam);
        }

        playerBidRepository.deleteById(playerBidId);

    }

    @Override
    public void deleteAllBids(Integer auctionId) {
        playerBidRepository.deleteByAuctionId(auctionId);
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
