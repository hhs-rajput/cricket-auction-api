package com.cricket.mpl.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "players", schema = "mpl")
@Data
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Integer playerId;

    @Column(name = "player_name")
    private String playerName;

    @Column(name = "player_role")
    private String playerRole;

    @Column(name = "base_price")
    private Integer basePrice;

    private boolean sold;

    @Column(name = "sold_price")
    private int soldPrice;

    @Column(name = "team_id")
    private Integer teamId;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "player_category")
    private String playerCategory;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "caption")
    private Boolean caption;

}
