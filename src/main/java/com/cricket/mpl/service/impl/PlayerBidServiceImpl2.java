//package com.cricket.mpl.service.impl;
//
//import com.cricket.mpl.dto.request.PlayerBidRequest;
//import com.cricket.mpl.dto.response.LiveAuctionCurrentPlayerResponseDTO;
//import com.cricket.mpl.dto.response.PlayerBidResponse;
//import com.cricket.mpl.entity.AuctionTeam;
//import com.cricket.mpl.entity.Player;
//import com.cricket.mpl.entity.PlayerBid;
//import com.cricket.mpl.entity.PlayerBidTransaction;
//import com.cricket.mpl.repository.AuctionTeamRepository;
//import com.cricket.mpl.repository.PlayerBidRepository;
//import com.cricket.mpl.repository.PlayerBidTransactionRepository;
//import com.cricket.mpl.repository.PlayerRepository;
//import com.cricket.mpl.service.PlayerBidService;
//import com.cricket.mpl.service.PlayerService;
//import jakarta.transaction.Transactional;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.Map;
//import java.util.concurrent.*;
//
//@Service
//public class PlayerBidServiceImpl2 implements PlayerBidService {
//
//    private Map<Integer, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
//
//    private final PlayerBidRepository playerBidRepository;
//    private final PlayerBidTransactionRepository playerBidTransactionRepository;
//    private final PlayerRepository playerRepository;
//    private final AuctionTeamRepository auctionTeamRepository;
//    private final AuctionWebSocketService auctionWebSocketService;
//    private final PlayerService playerService;
//    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//    public PlayerBidServiceImpl2(PlayerBidRepository playerBidRepository, PlayerBidTransactionRepository playerBidTransactionRepository, PlayerRepository playerRepository, AuctionTeamRepository auctionTeamRepository, AuctionWebSocketService auctionWebSocketService, PlayerService playerService) {
//        this.playerBidRepository = playerBidRepository;
//        this.playerBidTransactionRepository = playerBidTransactionRepository;
//        this.playerRepository = playerRepository;
//        this.auctionTeamRepository = auctionTeamRepository;
//        this.auctionWebSocketService = auctionWebSocketService;
//        this.playerService = playerService;
//    }
//
//    @Override
//    public void startBid(PlayerBidRequest playerBidRequest) {
//        PlayerBid existingBid = playerBidRepository.findByAuctionIdAndStatus(playerBidRequest.getAuctionId(), "BID_STARTED");
//        if (existingBid != null) {
//            throw new RuntimeException("A bid is already in progress for this auction.");
//        }
//        Player player = playerRepository.findById(playerBidRequest.getPlayerId()).get();
//        PlayerBid playerBid=new PlayerBid();
//        playerBid.setAuctionId(playerBidRequest.getAuctionId());
//        playerBid.setPlayerId(playerBidRequest.getPlayerId());
//        playerBid.setStatus("BID_STARTED");
//        playerBid.setPlayerBasePrice(playerBidRequest.getBasePrice());
//        playerBid.setAutoSale(playerBidRequest.getAutoSale());
//        playerBid.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
//        playerBid.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
//        playerBid.setCreatedBy(playerBidRequest.getUserId());
//        playerBid.setLastUpdatedBy(playerBidRequest.getUserId());
//        playerBid.setAutoSellTimeInSeconds(getAutoSellTimerSeconds(player.getPlayerCategory()));
//        playerBidRepository.save(playerBid);
//
//        if (playerBidRequest.getAutoSale()) {
//            scheduleAutoSell(playerBid);
//        }
//
//    }
//    private void scheduleAutoSell(PlayerBid playerBid) {
//
//        Integer bidId = playerBid.getPlayerBidId();
//
//        // 1. Cancel existing task
//        ScheduledFuture<?> existingTask = scheduledTasks.get(bidId);
//        if (existingTask != null && !existingTask.isDone()) {
//            existingTask.cancel(false);
//        }
//
//        // 2. Calculate delay from latest updatedAt
//        long delay = Duration.between(
//                LocalDateTime.now(ZoneId.of("Asia/Kolkata")),
//                playerBid.getUpdatedAt().plusSeconds(playerBid.getAutoSellTimeInSeconds())
//        ).toMillis();
//
//        if (delay < 0) delay = 0;
//
//        // 3. Schedule new task
//        ScheduledFuture<?> newTask = scheduler.schedule(() -> {
//
//            // 🔒 SAFETY CHECK
//            PlayerBid latest = playerBidRepository.findById(bidId).orElse(null);
//
//            if (latest != null) {
//                LocalDateTime expectedSellTime =
//                        latest.getUpdatedAt().plusSeconds(latest.getAutoSellTimeInSeconds());
//
//                if (LocalDateTime.now(ZoneId.of("Asia/Kolkata")).isAfter(expectedSellTime)) {
//                    playerService.sellPlayer(bidId);
//                }
//            }
//
//        }, delay, TimeUnit.MILLISECONDS);
//
//        // 4. Store task
//        scheduledTasks.put(bidId, newTask);
//    }
//
//    @Override
//    public LiveAuctionCurrentPlayerResponseDTO getCurrentPlayer(Integer auctionId, Integer teamId) {
//        PlayerBid playerBid = playerBidRepository.findByAuctionIdAndStatus(auctionId, "BID_STARTED");
//        if (playerBid!=null) {
//            AuctionTeam auctionTeam = auctionTeamRepository.findByAuctionIdAndTeamId(auctionId, playerBid.getLeadingTeamId());
//            Player player = playerRepository.findById(playerBid.getPlayerId()).get();
//            return getLiveAuctionCurrentPlayerResponseDTO(auctionId, playerBid, player,auctionTeam!=null?auctionTeam.getTeamName():null);
//        }else{
//            return null;
//        }
//    }
//
//    private static LiveAuctionCurrentPlayerResponseDTO getLiveAuctionCurrentPlayerResponseDTO(Integer auctionId, PlayerBid playerBid, Player player, String leadingTeamName) {
//        LiveAuctionCurrentPlayerResponseDTO responseDTO=new LiveAuctionCurrentPlayerResponseDTO();
//        responseDTO.setAuctionId(auctionId);
//        responseDTO.setCurrentBidAmount(playerBid.getBidAmount());
//        responseDTO.setPlayerId(playerBid.getPlayerId());
//        responseDTO.setPlayerBidId(playerBid.getPlayerBidId());
//        responseDTO.setPlayerName(player.getPlayerName());
//        responseDTO.setBasePrice(player.getBasePrice());
//        responseDTO.setPlayerRole(player.getPlayerRole());
//        responseDTO.setAutoSale(playerBid.getAutoSale());
//        responseDTO.setLeadingTeamName(leadingTeamName);
//        responseDTO.setLeadingTeamId(playerBid.getLeadingTeamId());
//        responseDTO.setCreatedAt(playerBid.getCreatedAt());
//        responseDTO.setPlayerCategory(player.getPlayerCategory());
//        responseDTO.setSellTime(playerBid.getAutoSellTimeInSeconds());
//        return responseDTO;
//    }
//
//    private static int getAutoSellTimerSeconds(String category) {
//        return switch (category) {
//            case "A" -> 90;
//            case "B" -> 60;
//            case "C" -> 45;
//            case "D" -> 30;
//            default -> throw new IllegalArgumentException("Invalid category: " + category);
//        };
//    }
//
//    @Override
//    @Transactional
//    public PlayerBidResponse updateBid(PlayerBidRequest playerBidRequest) {
//
//        PlayerBid playerBid = playerBidRepository.findById(playerBidRequest.getPlayerBidId()).get();
//        playerBid.setBidAmount(playerBidRequest.getBidAmount());
//        playerBid.setLastUpdatedBy(playerBidRequest.getUserId());
//        playerBid.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
//        playerBid.setLeadingTeamId(playerBidRequest.getTeamId());
//        PlayerBidTransaction playerBidTransaction = getPlayerBidTransaction(playerBidRequest);
//        playerBidRepository.save(playerBid);
//        playerBidTransactionRepository.save(playerBidTransaction);
//        // do below if player is sold to update the remaining purse of the team
//         AuctionTeam auctionTeam = auctionTeamRepository.findByAuctionIdAndTeamId(playerBidRequest.getAuctionId(), playerBidRequest.getTeamId());
//        //auctionTeam.setRemainingPurse(auctionTeam.getRemainingPurse() - playerBidRequest.getBidAmount());
//        //auctionTeamRepository.save(auctionTeam);
//
//        PlayerBidResponse playerBidResponse = PlayerBidResponse.builder()
//                .playerBidId(playerBid.getPlayerBidId())
//                .playerId(playerBid.getPlayerId())
//                .auctionId(playerBid.getAuctionId())
//                .leadingTeamId(playerBid.getLeadingTeamId())
//                .leadingTeamName(auctionTeam.getTeamName())
//                .currentBidAmount(playerBid.getBidAmount())
//                .updatedAt(playerBid.getUpdatedAt())
//                .sellTimeInSeconds(playerBid.getAutoSellTimeInSeconds())
//                .build();
//        auctionWebSocketService.sendBidUpdate(playerBidResponse);
//        if (playerBid.getAutoSale()) {
//            scheduleAutoSell(playerBid);
//        }
//        return playerBidResponse;
//
//
//    }
//
//    @Override
//    public boolean isBiddingOn(Integer auctionId) {
//        PlayerBid existingBid = playerBidRepository.findByAuctionIdAndStatus(auctionId, "BID_STARTED");
//        return existingBid != null;
//    }
//
//    @Override
//    public void cancelBid(Integer playerBidId) {
//        PlayerBid playerBid = playerBidRepository.findById(playerBidId).get();
//        playerBidRepository.deleteById(playerBidId);
//        Integer playerId = playerBid.getPlayerId();
//        Player player = playerRepository.findById(playerId).get();
//        if(player.isSold()){
//            int soldPrice = player.getSoldPrice();
//            Integer playerTeamId = player.getTeamId();
//            player.setSold(false);
//            player.setSoldPrice(0);
//            player.setTeamId(null);
//            playerRepository.save(player);
//            AuctionTeam auctionTeam = auctionTeamRepository.findByAuctionIdAndTeamId(playerBid.getAuctionId(), playerTeamId);
//            auctionTeam.setRemainingPurse(auctionTeam.getRemainingPurse()+soldPrice);
//            auctionTeamRepository.save(auctionTeam);
//        }
//
//        playerBidRepository.deleteById(playerBidId);
//
//    }
//
//    private static PlayerBidTransaction getPlayerBidTransaction(PlayerBidRequest playerBidRequest) {
//        PlayerBidTransaction playerBidTransaction=new PlayerBidTransaction();
//        playerBidTransaction.setBidAmount(playerBidRequest.getBidAmount());
//        playerBidTransaction.setAuctionId(playerBidRequest.getAuctionId());
//        playerBidTransaction.setPlayerBidId(playerBidRequest.getPlayerBidId());
//        playerBidTransaction.setPlayerBasePrice(playerBidRequest.getBasePrice());
//        playerBidTransaction.setPlayerId(playerBidRequest.getPlayerId());
//        playerBidTransaction.setTeamId(playerBidRequest.getTeamId());
//        playerBidTransaction.setCreatedBy(playerBidRequest.getUserId());
//        return playerBidTransaction;
//    }
//}
