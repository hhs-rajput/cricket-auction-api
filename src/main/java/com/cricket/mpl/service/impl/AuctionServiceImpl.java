package com.cricket.mpl.service.impl;

import com.cricket.mpl.dto.request.AuctionRequest;
import com.cricket.mpl.dto.response.AuctionResponseDTO;
import com.cricket.mpl.entity.Auction;
import com.cricket.mpl.repository.AuctionRepository;
import com.cricket.mpl.service.AuctionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;

    public AuctionServiceImpl(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    @Override
    public AuctionResponseDTO startAuction(Integer auctionId) {
        Auction auction = auctionRepository.findById(auctionId).get();
        auction.setIsActive(Boolean.TRUE);
        auction.setStatus("STARTED");
        auctionRepository.save(auction);
        return AuctionResponseDTO.builder().auctionId(auction.getAuctionId()).isActive(auction.getIsActive()).build();
    }

    @Override
    public AuctionResponseDTO getActiveAuction() {
        Auction activeAuction = auctionRepository.findByIsActiveTrue();
        if (activeAuction!=null) {
            return AuctionResponseDTO.builder().auctionId(activeAuction.getAuctionId()).isActive(activeAuction.getIsActive()).build();
        }else{
            return AuctionResponseDTO.builder().isActive(Boolean.FALSE).build();
        }
    }

    @Override
    public String createAuction(AuctionRequest auctionRequest) {
        Auction auction
                = new Auction();
        auction.setAuctionDate(auctionRequest.getAuctionDate());
        auction.setAuctionName(auctionRequest.getAuctionName());
        auction.setIsActive(Boolean.FALSE);
        auction.setStatus("CREATED");
        Auction saved = auctionRepository.save(auction);
        return "Auction Create Successfully with id "+saved.getAuctionId();
    }

    @Override
    public List<AuctionResponseDTO> getAllAuctions() {
        List<Auction> all = auctionRepository.findAll();
            if(!all.isEmpty()){
                return all.stream().map(auction -> AuctionResponseDTO.builder()
                        .auctionId(auction.getAuctionId())
                        .auctionDate(auction.getAuctionDate())
                        .auctionName(auction.getAuctionName())
                        .isActive(auction.getIsActive()).build()).toList();
            }
        return List.of();
    }

    @Override
    public AuctionResponseDTO complete(Integer auctionId) {
        Auction auction = auctionRepository.findById(auctionId).get();
        auction.setIsActive(Boolean.FALSE);
        auction.setStatus("COMPLETED");
        auctionRepository.save(auction);
        return AuctionResponseDTO.builder().auctionId(auction.getAuctionId()).isActive(auction.getIsActive()).build();
    }
}
