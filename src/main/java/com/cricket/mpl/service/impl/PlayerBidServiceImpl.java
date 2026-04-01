package com.cricket.mpl.service.impl;

import com.cricket.mpl.dto.request.PlayerBidRequest;
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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PlayerBidServiceImpl implements PlayerBidService {

    private final PlayerBidRepository playerBidRepository;
    private final PlayerBidTransactionRepository playerBidTransactionRepository;
    private final PlayerRepository playerRepository;
    private final AuctionTeamRepository auctionTeamRepository;
    private final AuctionWebSocketService auctionWebSocketService;

    public PlayerBidServiceImpl(PlayerBidRepository playerBidRepository, PlayerBidTransactionRepository playerBidTransactionRepository, PlayerRepository playerRepository, AuctionTeamRepository auctionTeamRepository, AuctionWebSocketService auctionWebSocketService) {
        this.playerBidRepository = playerBidRepository;
        this.playerBidTransactionRepository = playerBidTransactionRepository;
        this.playerRepository = playerRepository;
        this.auctionTeamRepository = auctionTeamRepository;
        this.auctionWebSocketService = auctionWebSocketService;
    }

    @Override
    public void startBid(PlayerBidRequest playerBidRequest) {
        PlayerBid playerBid=new PlayerBid();
        playerBid.setAuctionId(playerBidRequest.getAuctionId());
        playerBid.setPlayerId(playerBidRequest.getPlayerId());
        playerBid.setStatus("BID_STARTED");
        playerBid.setCreatedBy(playerBidRequest.getUserId());
        playerBid.setLastUpdatedBy((playerBidRequest.getUserId()));
        playerBid.setPlayerBasePrice(playerBidRequest.getBasePrice());
        playerBidRepository.save(playerBid);
    }

    @Override
    public LiveAuctionCurrentPlayerResponseDTO getCurrentPlayer(Integer auctionId, Integer teamId) {
        PlayerBid playerBid = playerBidRepository.findByAuctionIdAndStatus(auctionId, "BID_STARTED");
        AuctionTeam auctionTeam = auctionTeamRepository.findByAuctionIdAndTeamId(auctionId, playerBid.getLeadingTeamId());
        Player player = playerRepository.findById(playerBid.getPlayerId()).get();
        return getLiveAuctionCurrentPlayerResponseDTO(auctionId, playerBid, player,auctionTeam!=null?auctionTeam.getTeamName():null);
    }

    private static LiveAuctionCurrentPlayerResponseDTO getLiveAuctionCurrentPlayerResponseDTO(Integer auctionId, PlayerBid playerBid, Player player, String leadingTeamName) {
        LiveAuctionCurrentPlayerResponseDTO responseDTO=new LiveAuctionCurrentPlayerResponseDTO();
        responseDTO.setAuctionId(auctionId);
        responseDTO.setCurrentBidAmount(playerBid.getBidAmount());
        responseDTO.setPlayerId(playerBid.getPlayerId());
        responseDTO.setPlayerBidId(playerBid.getPlayerBidId());
        responseDTO.setPlayerName(player.getPlayerName());
        responseDTO.setBasePrice(player.getBasePrice());
        responseDTO.setPlayerRole(player.getPlayer_Role());
        responseDTO.setLeadingTeamName(leadingTeamName);
        responseDTO.setLeadingTeamId(playerBid.getLeadingTeamId());
        responseDTO.setPlayerCategory(player.getPlayerCategory());
        return responseDTO;
    }

    @Override
    @Transactional
    public PlayerBidResponse updateBid(PlayerBidRequest playerBidRequest) {

        PlayerBid playerBid = playerBidRepository.findById(playerBidRequest.getPlayerBidId()).get();
        playerBid.setBidAmount(playerBidRequest.getBidAmount());
        playerBid.setLastUpdatedBy(playerBidRequest.getUserId());
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
