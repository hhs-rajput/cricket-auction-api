package com.cricket.mpl.service.impl;

import com.cricket.mpl.dto.request.PlayerBidRequest;
import com.cricket.mpl.entity.PlayerBid;
import com.cricket.mpl.repository.PlayerBidRepository;
import com.cricket.mpl.service.PlayerBidService;
import org.springframework.stereotype.Service;

@Service
public class PlayerBidServiceImpl implements PlayerBidService {

    private final PlayerBidRepository playerBidRepository;

    public PlayerBidServiceImpl(PlayerBidRepository playerBidRepository) {
        this.playerBidRepository = playerBidRepository;
    }

    @Override
    public void addPlayerBid(PlayerBidRequest playerBidRequest) {
        PlayerBid playerBid=new PlayerBid();
        playerBid.setAuctionId(playerBidRequest.getAuctionId());
        playerBid.setTeamId(playerBidRequest.getTeamId());
        playerBid.setPlayerId(playerBidRequest.getPlayerId());
        playerBid.setBidAmount(playerBidRequest.getBidAmount());
        playerBid.setCaptionUserId(playerBidRequest.getCaptionUserId());
        playerBidRepository.save(playerBid);
    }
}
