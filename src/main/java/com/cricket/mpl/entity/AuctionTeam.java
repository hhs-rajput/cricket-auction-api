package com.cricket.mpl.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "auction_team_mapping" , schema = "mpl")
@Data
public class AuctionTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auction_team_mapping_id")
    private Integer auctionTeamId;

    @Column(name = "auction_id")
    private Integer auctionId;

    @Column(name = "team_id")
    private Integer teamId;

    @Column(name = "caption_user_id")
    private Integer captionUserId;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "total_purse")
    private Integer totalPurse;

    @Column(name = "remaining_purse")
    private Integer remainingPurse;

    @Column(name = "auction_completed")
    private boolean auctionCompleted;

}
