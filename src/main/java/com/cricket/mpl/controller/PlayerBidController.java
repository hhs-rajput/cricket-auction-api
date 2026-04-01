package com.cricket.mpl.controller;

import com.cricket.mpl.dto.request.PlayerBidRequest;
import com.cricket.mpl.service.PlayerBidService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/player/bid")
@CrossOrigin("*") // for React frontend
public class PlayerBidController {

    private final PlayerBidService playerBidService;

    public PlayerBidController(PlayerBidService playerBidService) {
        this.playerBidService = playerBidService;
    }


    @PostMapping
    public String startBid(@RequestBody PlayerBidRequest playerBidRequest) {
        playerBidService.startBid(playerBidRequest);
        return "Bid started successfully";
    }

}
