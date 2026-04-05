package com.cricket.mpl.controller;

import com.cricket.mpl.dto.request.LoginRequest;
import com.cricket.mpl.dto.request.UpdatePasswordRequest;
import com.cricket.mpl.dto.response.LoginResponse;
import com.cricket.mpl.entity.Team;
import com.cricket.mpl.entity.User;
import com.cricket.mpl.service.LoginService;
import com.cricket.mpl.service.TeamService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
@CrossOrigin("*")
public class LoginController {

    private final LoginService loginService;
    private final TeamService teamService;

    public LoginController(LoginService loginService, TeamService teamService) {
        this.loginService = loginService;
        this.teamService = teamService;
    }

    @PostMapping
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {



        System.out.println("Received login request for mobile: " + loginRequest.getMobile() + " and password: " + loginRequest.getPassword());
        User user = loginService.login(loginRequest);
        Team team = teamService.findByCaptainId(user.getUserId());

        return LoginResponse.builder()
                .userRole(user.getRole())
                .name(user.getName())
                .team(team!=null?team.getTeamName():null)
                .teamId(team!=null?team.getId():null)
                .teamStatus(team!=null?team.getTeamStatus():null)
                .userId(user.getUserId())
                .build();
    }

    @PostMapping("/reset-password")
    public String updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest){
        loginService.updatePassword(updatePasswordRequest);
        return "Password updated successfully";
    }
}
