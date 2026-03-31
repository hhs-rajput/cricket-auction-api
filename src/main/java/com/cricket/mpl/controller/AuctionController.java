package com.cricket.mpl.controller;

import com.cricket.mpl.dto.request.AuctionRequest;
import com.cricket.mpl.dto.response.ApiResponse;
import com.cricket.mpl.dto.response.AuctionResponseDTO;
import com.cricket.mpl.dto.response.AuctionTeamsResponseDTO;
import com.cricket.mpl.dto.response.LiveAuctionCurrentPlayerResponseDTO;
import com.cricket.mpl.service.AuctionService;
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

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;

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
    @GetMapping("/getActiveAuction")
    public AuctionResponseDTO getAuctionStatus() {
        return auctionService.getActiveAuction();
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

    @GetMapping("/teams")
    public List<AuctionTeamsResponseDTO> getAuctionTeams() {
        System.out.println("Received request to get Auction Teams.");
        return Arrays.asList(AuctionTeamsResponseDTO.builder()
                .teamId(1).teamName("India").purse(1000).build(),AuctionTeamsResponseDTO.builder()
                .teamId(1).teamName("Australia").purse(800).build(),AuctionTeamsResponseDTO.builder()
                .teamId(1).teamName("South Africa").purse(700).build(),AuctionTeamsResponseDTO.builder()
                .teamId(1).teamName("England").purse(750).build());
    }

    @GetMapping("/current-player")
    public LiveAuctionCurrentPlayerResponseDTO getLiveAuctionCurrentPlayerDetails() {
        System.out.println("Received request to get Auction Teams");
        return LiveAuctionCurrentPlayerResponseDTO.builder()
                .playerId(1)
                .playerName("Manish Pandey")
                .playerCategory("A")
                .basePrice(300)
                .playerRole("Right Handed Batter").build();
    }

}
