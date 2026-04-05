package com.cricket.mpl.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "auction" , schema = "mpl")
@Data
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auction_id")
    private Integer auctionId;

    @Column(name = "active")
    private Boolean isActive;
    @Column(name = "auction_name")
    private String auctionName;

    @Column(name = "status")
    private String status;

    @Column(name = "auto_sale")
    private Boolean autoSale;

    @Column(name = "auction_date")
    private LocalDateTime auctionDate;

}
