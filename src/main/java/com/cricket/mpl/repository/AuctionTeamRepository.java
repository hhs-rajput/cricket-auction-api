package com.cricket.mpl.repository;

import com.cricket.mpl.entity.Auction;
import com.cricket.mpl.entity.AuctionTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionTeamRepository extends JpaRepository<AuctionTeam,Integer> {

    AuctionTeam findByCaptionUserIdAndTeamId(Integer userId,Integer teamId);
}
