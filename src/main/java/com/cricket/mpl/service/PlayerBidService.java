package com.cricket.mpl.service;

import com.cricket.mpl.dto.request.PlayerBidRequest;
import com.cricket.mpl.dto.request.PlayerRequest;
import com.cricket.mpl.dto.response.PlayerResponseDto;

import java.util.List;

public interface PlayerBidService {
    void addPlayerBid(PlayerBidRequest playerBidRequest);
}
