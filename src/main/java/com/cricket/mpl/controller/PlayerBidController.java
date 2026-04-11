package com.cricket.mpl.controller;

import com.cricket.mpl.dto.request.AutoSellSettingsUpdateRequest;
import com.cricket.mpl.dto.request.PlayerBidRequest;
import com.cricket.mpl.dto.response.ApiResponse;
import com.cricket.mpl.dto.response.PlayerBidResponse;
import com.cricket.mpl.service.PlayerBidService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/player/bid")
@CrossOrigin("*") // for React frontend
public class PlayerBidController {

    private final PlayerBidService playerBidService;

    public PlayerBidController(PlayerBidService playerBidService) {
        this.playerBidService = playerBidService;
    }

    @GetMapping("/cancel")
    public String cancel(@RequestParam Integer playerBidId){
        playerBidService.cancelBid(playerBidId);
        return "Bid Cancelled successfully";
    }

    @PostMapping
    public String startBid(@RequestBody PlayerBidRequest playerBidRequest) {
        playerBidService.startBid(playerBidRequest);
        return "Bid started successfully";
    }

    @DeleteMapping("/delete/{auctionId}")
    public String deleteAllBids(@PathVariable Integer auctionId) {
        playerBidService.deleteAllBids(auctionId);
        return "All bids are delete successfully";
    }

    @PutMapping
    public PlayerBidResponse updateBid(@RequestBody PlayerBidRequest playerBidRequest) {
       return playerBidService.updateBid(playerBidRequest);
    }

    @PutMapping("/changeAutoSellSettings")
    public String  autoSellSettings(@RequestBody AutoSellSettingsUpdateRequest autoSellSettingsUpdateRequest) {
         playerBidService.autoSellSettings(autoSellSettingsUpdateRequest);
        return "Settings updated successfully!";
    }

    @GetMapping("/isBidding")
    public ResponseEntity<ApiResponse<Boolean>> isBiddingOn(@RequestParam Integer auctionId) {
        return playerBidService.isBiddingOn(auctionId)?ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "A bid is already in progress for this auction.", null)):
                ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(false, "There is no ongoing bid", null));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(RuntimeException ex) {
        if (ex.getMessage().contains("A bid is already in progress for this auction.")) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "An error occurred: " + ex.getMessage(), null));
    }

}
