package com.cricket.mpl.service;

import com.cricket.mpl.dto.request.AutoSellSettingsUpdateRequest;
import com.cricket.mpl.dto.request.PlayerBidRequest;
import com.cricket.mpl.dto.response.LiveAuctionCurrentPlayerResponseDTO;
import com.cricket.mpl.dto.response.PlayerBidResponse;

public interface PlayerBidService {
    void startBid(PlayerBidRequest playerBidRequest);

    LiveAuctionCurrentPlayerResponseDTO getCurrentPlayer(Integer auctionId, Integer teamId);

    PlayerBidResponse updateBid(PlayerBidRequest playerBidRequest);

    boolean isBiddingOn(Integer auctionId);

    void cancelBid(Integer playerBidId);

    void deleteAllBids(Integer auctionId);

    void autoSellSettings(AutoSellSettingsUpdateRequest autoSellSettingsUpdateRequest);
}
