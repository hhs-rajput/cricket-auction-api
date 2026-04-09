package com.cricket.mpl.controller;

import com.cricket.mpl.dto.request.TeamRequest;
import com.cricket.mpl.dto.response.MyTeamDetailsResponse;
import com.cricket.mpl.dto.response.TeamAndAuctionResponseDTO;
import com.cricket.mpl.dto.response.TeamAndCaptionResponseDTO;
import com.cricket.mpl.dto.response.TeamStatusResponseDTO;
import com.cricket.mpl.entity.Team;
import com.cricket.mpl.service.TeamService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team")
@CrossOrigin("*")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }



    @PostMapping("/create")
    public String createTeam(@RequestBody TeamRequest teamRequest) {
        teamService.createTeam(teamRequest);
        // Logic to create a team would go here
        return "Team created successfully!";
    }
    @GetMapping("/approve")
    public String approveTeam(@RequestParam Integer teamId, @RequestParam Integer captionUserId) {
        return teamService.approveTeam(teamId,captionUserId);
    }
    @GetMapping("/requests")
    public List<TeamAndCaptionResponseDTO> teamRequests() {
        return teamService.teamRequests();
    }

    @GetMapping()
    public MyTeamDetailsResponse myTeamDetails(@RequestParam Integer captionUserId) {
        return teamService.myTeamDetails(captionUserId);
    }
    @GetMapping("/all")
    public List<TeamAndCaptionResponseDTO> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/auctionMappings")
    public List<TeamAndAuctionResponseDTO> auctionMappings() {
        return teamService.auctionMappings();
    }

    @DeleteMapping("/auctionMappings/{auctionTeamId}")
    public String auctionMappings(@PathVariable Integer auctionTeamId) {
         return teamService.deleteAuctionMapping(auctionTeamId);
    }

    @PutMapping("/auctionMappings/{auctionTeamId}/{newPurse}")
    public String updateTeamAuctionRemainingPurse(@PathVariable Integer auctionTeamId,@PathVariable Integer newPurse) {
         return teamService.updateTeamAuctionRemainingPurse(auctionTeamId,newPurse);
    }



    @GetMapping("/status")
    public ResponseEntity<TeamStatusResponseDTO> getTeamStatus(
            @RequestParam Integer captionUserId) {

        Team team =  teamService.findByCaptainId(captionUserId);
        if (team!=null) {
            return ResponseEntity.ok(TeamStatusResponseDTO.builder()
                    .teamId(team.getId())
                    .teamName(team.getTeamName())
                    .teamStatus(team.getTeamStatus()).build());
        }
        return ResponseEntity.notFound().build();
    }
}
