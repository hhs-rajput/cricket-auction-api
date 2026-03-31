package com.cricket.mpl.repository;

import com.cricket.mpl.entity.PlayerBid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerBidRepository extends JpaRepository<PlayerBid,Integer> {
}
