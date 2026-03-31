package com.cricket.mpl.controller;

import com.cricket.mpl.dto.request.AuctionRequest;
import com.cricket.mpl.dto.response.AuctionResponseDTO;
import com.cricket.mpl.service.AuctionService;
import org.springframework.web.bind.annotation.*;

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
}
