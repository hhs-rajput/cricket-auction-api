package com.cricket.mpl.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "player_bid_transactions", schema = "mpl")
@Data
public class PlayerBidTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer transactionId;

    @Column(name = "player_bid_id", nullable = false)
    private Integer playerBidId;

    @Column(name = "auction_id", nullable = false)
    private Integer auctionId;

    @Column(name = "team_id")
    private Integer teamId;

    @Column(name = "player_id", nullable = false)
    private Integer playerId;

    @Column(name = "player_base_price", nullable = false)
    private Integer playerBasePrice;

    @Column(name = "bid_amount")
    private Integer bidAmount;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;
}
