package com.cricket.mpl.controller;

import com.cricket.mpl.dto.request.PlayerBidRequest;
import com.cricket.mpl.dto.request.PlayerRequest;
import com.cricket.mpl.dto.request.UserRequest;
import com.cricket.mpl.dto.response.PlayerCategoryResponseDto;
import com.cricket.mpl.dto.response.PlayerResponseDto;
import com.cricket.mpl.entity.User;
import com.cricket.mpl.service.PlayerBidService;
import com.cricket.mpl.service.PlayerService;
import com.cricket.mpl.service.UserService;
import com.cricket.mpl.util.Constants;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/player/bid")
@CrossOrigin("*") // for React frontend
public class PlayerBidController {

    private final PlayerBidService playerBidService;

    public PlayerBidController(PlayerBidService playerBidService) {
        this.playerBidService = playerBidService;
    }


    @PostMapping
    public String addPlayer(@RequestBody PlayerBidRequest playerBidRequest) {
        playerBidService.addPlayerBid(playerBidRequest);
        return "";
    }

}
