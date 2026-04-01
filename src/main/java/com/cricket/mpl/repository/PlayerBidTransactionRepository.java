package com.cricket.mpl.repository;

import com.cricket.mpl.entity.PlayerBidTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerBidTransactionRepository extends JpaRepository<PlayerBidTransaction, Integer> {

    List<PlayerBidTransaction> findByAuctionId(Integer auctionId);

    List<PlayerBidTransaction> findByPlayerBidId(Integer playerBidId);
}
