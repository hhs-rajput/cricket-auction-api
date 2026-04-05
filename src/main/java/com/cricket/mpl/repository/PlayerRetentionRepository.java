package com.cricket.mpl.repository;

import com.cricket.mpl.entity.PlayerRetention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRetentionRepository extends JpaRepository<PlayerRetention, Integer> {

    List<PlayerRetention> findByAuctionId(Integer auctionId);

    List<PlayerRetention> findByPlayerId(Integer playerId);

    List<PlayerRetention> findByTeamId(Integer teamId);

    List<PlayerRetention> findByStatus(String review);

    PlayerRetention findByAuctionIdAndTeamIdAndStatus(Integer auctionId, Integer teamId, String status);
}
