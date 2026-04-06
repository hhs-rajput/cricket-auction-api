package com.cricket.mpl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "player_bid", schema = "mpl")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerBid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_bid_id")
    private Integer playerBidId;

    @Column(name = "auction_id", nullable = false)
    private Integer auctionId;

    @Column(name = "leading_team_id")
    private Integer leadingTeamId;

    @Column(name = "player_id", nullable = false)
    private Integer playerId;

    @Column(name = "bid_amount")
    private Integer bidAmount;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "last_updated_by")
    private Integer lastUpdatedBy;

    @Column(name = "player_base_price")
    private Integer playerBasePrice;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "auto_sale",nullable = false)
    private Boolean autoSale;

    @Column(name = "auto_sell_time_in_seconds")
    private Integer autoSellTimeInSeconds;


}
