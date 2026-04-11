package com.cricket.mpl.repository;

import com.cricket.mpl.entity.PlayerBid;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerBidRepository extends JpaRepository<PlayerBid,Integer> {
    PlayerBid findByAuctionIdAndStatus(Integer auctionId, String bidStarted);

    PlayerBid findByAuctionIdAndLeadingTeamIdAndStatus(Integer auctionId, Integer teamId, String bidStarted);

    @Transactional
    void deleteByAuctionId(Integer auctionId);
}
