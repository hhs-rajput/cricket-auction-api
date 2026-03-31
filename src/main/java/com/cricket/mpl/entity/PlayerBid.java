package com.cricket.mpl.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "player_bid", schema = "mpl")
@Data
public class PlayerBid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_id")
    private Integer bidId;

    @Column(name = "player_id", nullable = false)
    private Integer playerId;

    @Column(name = "team_id", nullable = false)
    private Integer teamId;

    @Column(name = "auction_id", nullable = false)
    private Integer auctionId;

    @Column(name = "caption_user_id", nullable = false)
    private Integer captionUserId;

    @Column(name = "bid_amount", nullable = false)
    private Integer bidAmount;

}
