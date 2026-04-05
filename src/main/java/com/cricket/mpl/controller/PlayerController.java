package com.cricket.mpl.controller;

import com.cricket.mpl.dto.request.*;
import com.cricket.mpl.dto.response.*;
import com.cricket.mpl.entity.User;
import com.cricket.mpl.service.PlayerService;
import com.cricket.mpl.service.UserService;
import com.cricket.mpl.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/players")
@CrossOrigin("*") // for React frontend
public class PlayerController {

    private final PlayerService playerService;
    private final UserService userService;

    public PlayerController(PlayerService playerService, UserService userService) {
        this.playerService = playerService;
        this.userService = userService;
    }

    @GetMapping
    public List<PlayerResponseDto> getAllPlayers() {
        System.out.println("Fetching all players...");
        return playerService.getAllPlayers();
    }

    @GetMapping("/by-category")
    public List<PlayerCategoryResponseDto> getAllPlayersByCategories(@RequestParam String category) {
        System.out.println("Fetching all players by category");
        List<PlayerResponseDto> allPlayers = playerService.getAllPlayers(category);
        return allPlayers.stream().map(player -> {
            PlayerCategoryResponseDto dto = new PlayerCategoryResponseDto();
            dto.setPlayerId(player.getPlayerId());
            dto.setPlayerName(player.getPlayerName());
            dto.setPlayerCategory(player.getPlayerCategory());
            dto.setBasePrice(player.getBasePrice());
            return dto;
        }).collect(Collectors.toList());
    }
    @GetMapping("/team/{teamId}")
    public List<PlayerResponseDto> getAllPlayersByTeam(@PathVariable Integer teamId) {
        System.out.println("Fetching all players by team");
        return playerService.getAllPlayersByTeam(teamId);

    }

    @PostMapping
    public String addPlayer(@RequestBody PlayerRequest playerRequest) {
        System.out.println("Received request to add player: " + playerRequest.getPlayerName());
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(playerRequest.getPhoneNumber());
        userRequest.setName(playerRequest.getPlayerName());
        userRequest.setPassword(playerRequest.getPhoneNumber());
        userRequest.setRole(Constants.USER_ROLE_PLAYER.getValue());
        User user = userService.addUser(userRequest);
        playerRequest.setUserId(user.getUserId());
        playerService.addPlayer(playerRequest);

        return "Player " + playerRequest.getPlayerCategory() + " added successfully!";
    }

    @PostMapping("/sell")
    public PlayerSoldDto sell(@RequestBody SellPlayerRequest sellPlayerRequest) {
        return playerService.sellPlayer(sellPlayerRequest);
    }

    @PostMapping("/retain")
    public ResponseEntity<ApiResponse<String>> retainPlayer(@RequestBody RetainPlayerRequest retainPlayerRequest) {
        playerService.retainPlayer(retainPlayerRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Retention Request sent to admin Successfully!", null));
    }

    @GetMapping("/retain/requests")
    public List<RetainRequestsResponseDto> getAllRetainRequests() {
        System.out.println("Updating  retain request.");
        return playerService.getAllRetainRequests();
    }

    @PutMapping("/retain/approve")
    public String retainApprove(@RequestBody RetainApproveRequest retainApproveRequest) {
        System.out.println("Fetching all retain requests.");
        playerService.retainApprove(retainApproveRequest);
        return "";

    }

    @PutMapping("/retain/reject")
    public String retainReject(@RequestBody RetainApproveRequest retainApproveRequest) {
        System.out.println("Fetching all retain requests.");
        playerService.retainReject(retainApproveRequest);
        return "";

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(RuntimeException ex) {
        if (ex.getMessage().contains("You cannot retain more than one player for the same auction")) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "An error occurred: " + ex.getMessage(), null));
    }

}
