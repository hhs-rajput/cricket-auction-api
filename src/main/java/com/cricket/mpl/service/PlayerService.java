package com.cricket.mpl.service;

import com.cricket.mpl.dto.request.PlayerRequest;
import com.cricket.mpl.dto.request.RetainApproveRequest;
import com.cricket.mpl.dto.request.RetainPlayerRequest;
import com.cricket.mpl.dto.request.SellPlayerRequest;
import com.cricket.mpl.dto.response.PlayerResponseDto;
import com.cricket.mpl.dto.response.PlayerSoldDto;
import com.cricket.mpl.dto.response.RetainRequestsResponseDto;

import java.util.List;

public interface PlayerService {
    List<PlayerResponseDto> getAllPlayers();

    void addPlayer(PlayerRequest player);
    void addPlayer(PlayerRequest player,Integer userId);

    List<PlayerResponseDto> getAllPlayersByTeam(Integer teamId);

    List<PlayerResponseDto> getAllPlayers(String category);

    PlayerSoldDto sellPlayer(SellPlayerRequest sellPlayerRequest);

    void retainPlayer(RetainPlayerRequest retainPlayerRequest);

    List<RetainRequestsResponseDto> getAllRetainRequests();

    void retainApprove(RetainApproveRequest retainApproveRequest);

    void retainReject(RetainApproveRequest retainApproveRequest);
}
