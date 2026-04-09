package com.cricket.mpl.repository;

import com.cricket.mpl.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction,Integer> {

    List<Auction> findByIsActiveTrue();

    List<Auction> findByStatusAndAuctionDateAfter(String created, LocalDateTime now);
    List<Auction> findByStatusIn(List<String> statuses);
}
