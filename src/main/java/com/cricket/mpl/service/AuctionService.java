package com.cricket.mpl.service;

import com.cricket.mpl.dto.request.AuctionRegisterRequest;
import com.cricket.mpl.dto.request.AuctionRequest;
import com.cricket.mpl.dto.response.AuctionResponseDTO;
import com.cricket.mpl.dto.response.AuctionTeamsResponseDTO;

import java.util.List;

public interface AuctionService {
   AuctionResponseDTO startAuction(Integer auctionId);

   List<AuctionResponseDTO> getActiveAuctions();

   String createAuction(AuctionRequest auctionRequest);

   List<AuctionResponseDTO> getAllAuctions();

   AuctionResponseDTO getUserAuction(Integer userId, Integer teamId);

   AuctionResponseDTO complete(Integer auctionId);

   List<AuctionResponseDTO> getUpcomingAuctions();

   String register(AuctionRegisterRequest auctionRegisterRequest);

   List<AuctionTeamsResponseDTO> getAuctionTeams(Integer auctionId);
}
