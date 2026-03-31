package com.cricket.mpl.repository;

import com.cricket.mpl.entity.Auction;
import com.cricket.mpl.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends JpaRepository<Auction,Integer> {

    Auction findByIsActiveTrue();
}
