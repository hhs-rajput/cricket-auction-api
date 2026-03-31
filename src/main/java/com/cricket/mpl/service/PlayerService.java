package com.cricket.mpl.service;

import com.cricket.mpl.dto.request.PlayerRequest;
import com.cricket.mpl.dto.response.PlayerResponseDto;

import java.util.List;

public interface PlayerService {
    List<PlayerResponseDto> getAllPlayers();

    void addPlayer(PlayerRequest player);
    void addPlayer(PlayerRequest player,Integer userId);

    List<PlayerResponseDto> getAllPlayersByTeam(Integer teamId);
}
