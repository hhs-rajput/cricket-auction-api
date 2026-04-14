package com.cricket.mpl.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "auto_sale_settings", schema = "mpl")
public class AutoSaleSettings {

    // Getters and Setters
    @Id
    @Column(name = "player_category", length = 1, nullable = false)
    private String playerCategory;

    @Column(name = "seconds", nullable = false)
    private Integer seconds;

    // Constructors
    public AutoSaleSettings() {}

    public AutoSaleSettings(String playerCategory, Integer seconds) {
        this.playerCategory = playerCategory;
        this.seconds = seconds;
    }

    public void setPlayerCategory(String playerCategory) {
        this.playerCategory = playerCategory;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }
}
