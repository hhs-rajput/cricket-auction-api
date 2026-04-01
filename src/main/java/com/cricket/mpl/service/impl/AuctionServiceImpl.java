package com.cricket.mpl.service.impl;

import com.cricket.mpl.dto.request.AuctionRegisterRequest;
import com.cricket.mpl.dto.request.AuctionRequest;
import com.cricket.mpl.dto.response.AuctionResponseDTO;
import com.cricket.mpl.entity.Auction;
import com.cricket.mpl.entity.AuctionTeam;
import com.cricket.mpl.repository.AuctionRepository;
import com.cricket.mpl.repository.AuctionTeamRepository;
import com.cricket.mpl.service.AuctionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;
    private final AuctionTeamRepository auctionTeamRepository;

    public AuctionServiceImpl(AuctionRepository auctionRepository, AuctionTeamRepository auctionTeamRepository) {
        this.auctionRepository = auctionRepository;
        this.auctionTeamRepository = auctionTeamRepository;
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
                        .status(auction.getStatus())
                        .isActive(auction.getIsActive()).build()).toList();
            }
        return List.of();
    }

    @Override
    public AuctionResponseDTO getUserAuction(Integer userId, Integer teamId) {
        AuctionTeam auctionTeam = auctionTeamRepository.findByCaptionUserIdAndTeamId(userId,teamId);
        Auction auction = null;
        if (auctionTeam != null) {
            auction = auctionRepository.findById(auctionTeam.getAuctionId()).get();
            return AuctionResponseDTO.builder()
                    .auctionId(auction.getAuctionId())
                    .auctionName(auction.getAuctionName())
                    .auctionDate(auction.getAuctionDate())
                    .status(auction.getStatus())
                    .isActive(auction.getIsActive())
                    .build();
        }
        return null;
    }

    @Override
    public AuctionResponseDTO complete(Integer auctionId) {
        Auction auction = auctionRepository.findById(auctionId).get();
        auction.setIsActive(Boolean.FALSE);
        auction.setStatus("COMPLETED");
        auctionRepository.save(auction);
        return AuctionResponseDTO.builder().auctionId(auction.getAuctionId()).isActive(auction.getIsActive()).build();
    }

    @Override
    public List<AuctionResponseDTO> getUpcomingAuctions() {
        List<Auction> all = auctionRepository.findByStatusAndAuctionDateAfter("NOT_STARTED",java.time.LocalDateTime.now());
        if(!all.isEmpty()){
            return all.stream().map(auction -> AuctionResponseDTO.builder()
                    .auctionId(auction.getAuctionId())
                    .auctionDate(auction.getAuctionDate())
                    .auctionName(auction.getAuctionName())
                    .status(auction.getStatus())
                    .isActive(auction.getIsActive()).build()).toList();
        }
        return List.of();
    }

    @Override
    public String register(AuctionRegisterRequest auctionRegisterRequest) {
        AuctionTeam auctionTeam=new AuctionTeam();
        auctionTeam.setAuctionId(auctionRegisterRequest.getAuctionId());
        auctionTeam.setTeamId(auctionRegisterRequest.getTeamId());
        auctionTeam.setCaptionUserId(auctionRegisterRequest.getCaptionUserId());
        auctionTeamRepository.save(auctionTeam);
        return "Registered Successfully";
    }
}
