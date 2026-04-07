package com.cricket.mpl.service.impl;

import com.cricket.mpl.dto.request.AuctionRegisterRequest;
import com.cricket.mpl.dto.request.AuctionRequest;
import com.cricket.mpl.dto.response.AuctionResponseDTO;
import com.cricket.mpl.dto.response.AuctionTeamsResponseDTO;
import com.cricket.mpl.entity.Auction;
import com.cricket.mpl.entity.AuctionTeam;
import com.cricket.mpl.repository.AuctionRepository;
import com.cricket.mpl.repository.AuctionTeamRepository;
import com.cricket.mpl.service.AuctionService;
import com.cricket.mpl.service.TeamService;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;

@Service
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;
    private final AuctionTeamRepository auctionTeamRepository;
    private final TeamService teamService;

    public AuctionServiceImpl(AuctionRepository auctionRepository, AuctionTeamRepository auctionTeamRepository, TeamService teamService) {
        this.auctionRepository = auctionRepository;
        this.auctionTeamRepository = auctionTeamRepository;
        this.teamService = teamService;
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
    public List<AuctionResponseDTO> getActiveAuctions() {
        List<Auction> activeAuctions = auctionRepository.findByIsActiveTrue();
        if(!activeAuctions.isEmpty()){
            return activeAuctions.stream().map(auction -> AuctionResponseDTO.builder()
                    .auctionId(auction.getAuctionId())
                    .auctionDate(auction.getAuctionDate())
                    .auctionName(auction.getAuctionName())
                    .status(auction.getStatus())
                    .autoSale(auction.getAutoSale())
                    .isActive(auction.getIsActive()).build()).toList();
        }
        return null;
    }

    @Override
    public String createAuction(AuctionRequest auctionRequest) {
        Auction auction
                = new Auction();
        auction.setAuctionDate(auctionRequest.getAuctionDate());
        auction.setAuctionName(auctionRequest.getAuctionName());
        auction.setIsActive(Boolean.FALSE);
        auction.setStatus("NOT_STARTED");
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
                        .autoSale(auction.getAutoSale())
                        .isActive(auction.getIsActive()).build()).toList();
            }
        return List.of();
    }

    @Override
    public AuctionResponseDTO getUserAuction(Integer userId, Integer teamId) {
        AuctionTeam auctionTeam = auctionTeamRepository.findByCaptionUserIdAndTeamIdAndAuctionCompleted(userId,teamId,Boolean.FALSE);
        Auction auction = auctionRepository.findById(auctionTeam.getAuctionId()).get();
        if(auction.getStatus().equals("COMPLETED")) return null;

        if (auctionTeam != null) {
            auction = auctionRepository.findById(auctionTeam.getAuctionId()).get();
            return AuctionResponseDTO.builder()
                    .auctionId(auction.getAuctionId())
                    .auctionName(auction.getAuctionName())
                    .auctionDate(auction.getAuctionDate())
                    .status(auction.getStatus())
                    .autoSale(auction.getAutoSale())
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
        List<Auction> all = auctionRepository.findByStatusAndAuctionDateAfter("NOT_STARTED",java.time.LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
        if(!all.isEmpty()){
            return all.stream().map(auction -> AuctionResponseDTO.builder()
                    .auctionId(auction.getAuctionId())
                    .auctionDate(auction.getAuctionDate())
                    .auctionName(auction.getAuctionName())
                    .status(auction.getStatus())
                    .autoSale(auction.getAutoSale())
                    .isActive(auction.getIsActive()).build()).toList();
        }
        return List.of();
    }

    @Override
    public String register(AuctionRegisterRequest auctionRegisterRequest) {
        AuctionTeam existingAuctionTeam = auctionTeamRepository.findByCaptionUserIdAndTeamIdAndAuctionCompleted(auctionRegisterRequest.getCaptionUserId(), auctionRegisterRequest.getTeamId(),Boolean.FALSE);
        if(existingAuctionTeam!=null){
            return "You have already registered for a different auction.";
        }
        AuctionTeam auctionTeam=new AuctionTeam();
        auctionTeam.setAuctionId(auctionRegisterRequest.getAuctionId());
        auctionTeam.setTeamId(auctionRegisterRequest.getTeamId());
        auctionTeam.setCaptionUserId(auctionRegisterRequest.getCaptionUserId());
        auctionTeam.setTotalPurse(100);
        auctionTeam.setTeamName(auctionRegisterRequest.getTeamName());
        auctionTeam.setRemainingPurse(100);
        auctionTeamRepository.save(auctionTeam);
        return "Registered Successfully";
    }

    @Override
    public List<AuctionTeamsResponseDTO> getAuctionTeams(Integer auctionId) {
        List<AuctionTeam> auctionTeams = auctionTeamRepository.findByAuctionId(auctionId);
        if(!auctionTeams.isEmpty()){
            return auctionTeams.stream().map(auctionTeam -> AuctionTeamsResponseDTO.builder()
                    .teamId(auctionTeam.getTeamId())
                    .teamName(auctionTeam.getTeamName())
                    .purse(auctionTeam.getTotalPurse())
                    .remainingPurse(auctionTeam.getRemainingPurse())
                    .captionUserId(auctionTeam.getCaptionUserId())
                    .build()).toList();
        }
        return null;
    }

    @Override
    public String updateAuction(Integer auctionId, String action) {
        Auction auction = auctionRepository.findById(auctionId).get();
        auction.setAutoSale(action.equals("ON"));
        auctionRepository.save(auction);
        return "Auto sale has been set to "+action.equals("ON");
    }
}
