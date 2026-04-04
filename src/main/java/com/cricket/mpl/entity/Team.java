package com.cricket.mpl.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "teams", schema = "mpl")
@Data
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "team_size")
    private Integer teamSize;

    @Column(name = "caption_user_id")
    private Integer captionUserId;

    @Column(name = "team_status")
    private String teamStatus;

    @Column(name = "auction_joined")
    private boolean actionJoined;

}
