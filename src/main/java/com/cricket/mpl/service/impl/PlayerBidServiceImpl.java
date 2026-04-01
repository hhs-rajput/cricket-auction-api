package com.cricket.mpl.service.impl;

import com.cricket.mpl.dto.request.PlayerBidRequest;
import com.cricket.mpl.dto.response.LiveAuctionCurrentPlayerResponseDTO;
import com.cricket.mpl.entity.Player;
import com.cricket.mpl.entity.PlayerBid;
import com.cricket.mpl.repository.PlayerBidRepository;
import com.cricket.mpl.repository.PlayerRepository;
import com.cricket.mpl.service.PlayerBidService;
import com.cricket.mpl.service.PlayerService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerBidServiceImpl implements PlayerBidService {

    private final PlayerBidRepository playerBidRepository;
    private final PlayerRepository playerRepository;

    public PlayerBidServiceImpl(PlayerBidRepository playerBidRepository, PlayerRepository playerRepository) {
        this.playerBidRepository = playerBidRepository;
        this.playerRepository = playerRepository;
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
    public LiveAuctionCurrentPlayerResponseDTO getCurrentPlayer(Integer auctionId) {
        PlayerBid playerBid = playerBidRepository.findByAuctionIdAndStatus(auctionId, "BID_STARTED");
        if(playerBid!=null){
            Player player = playerRepository.findById(playerBid.getPlayerId()).get();
            LiveAuctionCurrentPlayerResponseDTO responseDTO=new LiveAuctionCurrentPlayerResponseDTO();
            responseDTO.setPlayerId(playerBid.getPlayerId());
            responseDTO.setPlayerName(player.getPlayerName());
            responseDTO.setBasePrice(playerBid.getPlayerBasePrice());
            responseDTO.setPlayerRole(player.getPlayer_Role());
            responseDTO.setPlayerCategory(player.getPlayerCategory());
            return responseDTO;
        }
        return null;
    }
}
