package com.cricket.mpl.repository;

import com.cricket.mpl.entity.AuctionTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionTeamRepository extends JpaRepository<AuctionTeam,Integer> {

    AuctionTeam findByCaptionUserIdAndTeamId(Integer userId,Integer teamId);

    List<AuctionTeam> findByAuctionId(Integer auctionId);

    AuctionTeam findByAuctionIdAndTeamId(Integer auctionId, Integer teamId);

    AuctionTeam findByCaptionUserIdAndTeamIdAndAuctionCompleted(Integer captionUserId, Integer teamId, boolean auctionCompleted);

    AuctionTeam findByTeamIdAndAuctionCompleted(Integer teamId, boolean auctionCompleted);
}
