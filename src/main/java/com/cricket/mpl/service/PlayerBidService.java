package com.cricket.mpl.service;

import com.cricket.mpl.dto.request.PlayerBidRequest;
import com.cricket.mpl.dto.response.LiveAuctionCurrentPlayerResponseDTO;

public interface PlayerBidService {
    void startBid(PlayerBidRequest playerBidRequest);

    LiveAuctionCurrentPlayerResponseDTO getCurrentPlayer(Integer auctionId);
}
