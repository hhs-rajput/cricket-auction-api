package com.cricket.mpl.mapper;

import com.cricket.mpl.dto.request.PlayerRequest;
import com.cricket.mpl.dto.response.PlayerResponseDto;
import com.cricket.mpl.entity.Player;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PlayerMapper {

    public Player playerRequestDTOToPlayer(PlayerRequest playerRequest) {
        Player player = new Player();
        player.setPlayerName(playerRequest.getPlayerName());
        player.setPhoneNumber(playerRequest.getPhoneNumber());
        player.setBasePrice(playerRequest.getBasePrice());
        player.setSold(playerRequest.isSold());
        player.setSoldPrice(playerRequest.getSoldPrice());
        player.setTeamId(playerRequest.getTeam());
        player.setPlayerCategory(playerRequest.getPlayerCategory());
        player.setUserId(playerRequest.getUserId());
        return player;
    }

    public List<PlayerResponseDto> playerEntityToPlayerResponseDTOList(List<Player> allPlayers, Map<Integer, String> teamIdNameMap) {

        return allPlayers.stream().map(player -> {
            PlayerResponseDto responseDto = new PlayerResponseDto();
            responseDto.setPlayerId(player.getPlayerId());
            responseDto.setPlayerName(player.getPlayerName());
            responseDto.setBasePrice(player.getBasePrice());
            responseDto.setSold(player.isSold());
            responseDto.setSoldPrice(player.getSoldPrice());
            responseDto.setCaption(player.getCaption());
            if (teamIdNameMap!=null && player.getTeamId() != null) {
                responseDto.setTeam(teamIdNameMap.getOrDefault(player.getTeamId(),""));
            }
            responseDto.setPlayerCategory(player.getPlayerCategory());
            return responseDto;
        }).toList();
    }
    public List<PlayerResponseDto> playerEntityToPlayerResponseDTOList(List<Player> allPlayers) {

        return allPlayers.stream().map(player -> {
            PlayerResponseDto responseDto = new PlayerResponseDto();
            responseDto.setPlayerId(player.getPlayerId());
            responseDto.setPlayerName(player.getPlayerName());
            responseDto.setBasePrice(player.getBasePrice());
            responseDto.setSold(player.isSold());
            responseDto.setSoldPrice(player.getSoldPrice());
            responseDto.setCaption(player.getCaption());
            responseDto.setPhoneNumber(player.getPhoneNumber());
            responseDto.setPlayerCategory(player.getPlayerCategory());
            return responseDto;
        }).toList();
    }
}
