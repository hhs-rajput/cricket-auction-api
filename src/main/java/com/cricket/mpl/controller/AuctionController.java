package com.cricket.mpl.controller;

import com.cricket.mpl.dto.request.AuctionRegisterRequest;
import com.cricket.mpl.dto.request.AuctionRequest;
import com.cricket.mpl.dto.response.ApiResponse;
import com.cricket.mpl.dto.response.AuctionResponseDTO;
import com.cricket.mpl.dto.response.AuctionTeamsResponseDTO;
import com.cricket.mpl.dto.response.LiveAuctionCurrentPlayerResponseDTO;
import com.cricket.mpl.service.AuctionService;
import com.cricket.mpl.service.PlayerBidService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/auction")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuctionController {

    private final AuctionService auctionService;
    private final PlayerBidService playerBidService;

    public AuctionController(AuctionService auctionService, PlayerBidService playerBidService) {
        this.auctionService = auctionService;

        this.playerBidService = playerBidService;
    }

    @GetMapping
    public List<AuctionResponseDTO> getAllAuctions() {
        return auctionService.getAllAuctions();
    }

    @GetMapping("/upcoming")
    public List<AuctionResponseDTO> getUpcomingAuctions() {
        return auctionService.getUpcomingAuctions();
    }

    @GetMapping("/userAuctionDetails")
    public ResponseEntity<ApiResponse<AuctionResponseDTO>> getUserAuction(@RequestParam Integer userId,@RequestParam Integer teamId) {

        AuctionResponseDTO userAuction = auctionService.getUserAuction(userId,teamId);
        if(userAuction!=null){
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Auction Found", userAuction)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "No User Auction Found", null));
    }
    @GetMapping("/getActiveAuctions")
    public List<AuctionResponseDTO> getAuctionStatus() {
        return auctionService.getActiveAuctions();
    }

    @PutMapping("/start/{auctionId}")
    public AuctionResponseDTO start(@PathVariable Integer auctionId) {
        System.out.println("Received request to start auction");
        return auctionService.startAuction(auctionId);
    }

    @PutMapping("/complete/{auctionId}")
    public AuctionResponseDTO complete(@PathVariable Integer auctionId) {
        System.out.println("Received request to start auction");
        return auctionService.complete(auctionId);
    }

    @PostMapping
    public String create(@RequestBody AuctionRequest auctionRequest){
        return auctionService.createAuction(auctionRequest);
    }

    @PostMapping("/register")
    public String register(@RequestBody AuctionRegisterRequest auctionRegisterRequest){
        return auctionService.register(auctionRegisterRequest);
    }

    @GetMapping("/teams")
    public List<AuctionTeamsResponseDTO> getAuctionTeams(@RequestParam Integer auctionId) {
        System.out.println("Received request to get Auction Teams.");
        return auctionService.getAuctionTeams(auctionId);
    }

    @GetMapping("/current-player")
    public LiveAuctionCurrentPlayerResponseDTO getLiveAuctionCurrentPlayerDetails(@RequestParam Integer auctionId) {
        return playerBidService.getCurrentPlayer(auctionId);
    }

}
