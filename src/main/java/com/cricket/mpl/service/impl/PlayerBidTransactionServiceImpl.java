package com.cricket.mpl.service.impl;

import com.cricket.mpl.dto.request.PlayerBidTransactionRequestDto;
import com.cricket.mpl.entity.PlayerBidTransaction;
import com.cricket.mpl.repository.PlayerBidTransactionRepository;
import com.cricket.mpl.service.PlayerBidTransactionService;

public class PlayerBidTransactionServiceImpl implements PlayerBidTransactionService {

    private final PlayerBidTransactionRepository playerBidTransactionRepository;

    public PlayerBidTransactionServiceImpl(PlayerBidTransactionRepository playerBidTransactionRepository) {
        this.playerBidTransactionRepository = playerBidTransactionRepository;
    }

    @Override
    public PlayerBidTransaction createTransaction(PlayerBidTransactionRequestDto dto) {
        PlayerBidTransaction entity = new PlayerBidTransaction();

        entity.setPlayerBidId(dto.getPlayerBidId());
        entity.setAuctionId(dto.getAuctionId());
        entity.setTeamId(dto.getTeamId());
        entity.setPlayerId(dto.getPlayerId());
        entity.setPlayerBasePrice(dto.getPlayerBasePrice());
        entity.setBidAmount(dto.getBidAmount());
        entity.setCreatedBy(dto.getCreatedBy());

        return playerBidTransactionRepository.save(entity);
    }
}
