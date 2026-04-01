package com.cricket.mpl.service;

import com.cricket.mpl.dto.request.PlayerBidTransactionRequestDto;
import com.cricket.mpl.entity.PlayerBidTransaction;

public interface PlayerBidTransactionService {
    PlayerBidTransaction createTransaction(PlayerBidTransactionRequestDto dto);
}
