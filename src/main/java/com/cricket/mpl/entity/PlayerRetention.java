package com.cricket.mpl.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "player_retention", schema = "mpl")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerRetention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "retention_id")
    private Integer retentionId;

    @Column(name = "player_id", nullable = false)
    private Integer playerId;

    @Column(name = "player_name", nullable = false)
    private String playerName;

    @Column(name = "team_id", nullable = false)
    private Integer teamId;

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @Column(name = "auction_id", nullable = false)
    private Integer auctionId;

    @Column(name = "auction_name", nullable = false)
    private String auctionName;

    @Column(name = "status")
    private String status;

    @Column(name = "retain_price")
    private Integer retainPrice;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(name = "updated_by", nullable = false)
    private Integer updatedBy;

}
