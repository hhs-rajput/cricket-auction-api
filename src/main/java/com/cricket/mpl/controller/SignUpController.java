package com.cricket.mpl.controller;

import com.cricket.mpl.dto.request.PlayerRequest;
import com.cricket.mpl.dto.request.SignUpRequest;
import com.cricket.mpl.dto.request.UserRequest;
import com.cricket.mpl.entity.User;
import com.cricket.mpl.service.PlayerService;
import com.cricket.mpl.service.UserService;
import com.cricket.mpl.util.Constants;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signup")
@CrossOrigin("*")
public class SignUpController {

    private final UserService userService;
    private final PlayerService playerService;

    public SignUpController(UserService userService, PlayerService playerService) {
        this.userService = userService;
        this.playerService = playerService;
    }

    @PostMapping
    public String signup(@RequestBody SignUpRequest signUpRequest){
        System.out.println("Received signup request for name: " + signUpRequest.getName() + ", mobile: " + signUpRequest.getMobile() + ", password: " + signUpRequest.getPassword());
        User user = userService.addUser(toUserRequest(signUpRequest));
        playerService.addPlayer(toPlayerRequest(signUpRequest),user.getUserId());
        return "User " + signUpRequest.getName() + " signed up successfully!";
    }

    private PlayerRequest toPlayerRequest(SignUpRequest signUpRequest) {
        PlayerRequest playerRequest = new PlayerRequest();
        playerRequest.setPlayerName(signUpRequest.getName());
        playerRequest.setPhoneNumber(signUpRequest.getMobile());
        return playerRequest;
    }

    private UserRequest toUserRequest(SignUpRequest signUpRequest) {
        UserRequest userRequest = new UserRequest();
        userRequest.setName(signUpRequest.getName());
        userRequest.setPassword(signUpRequest.getPassword());
        userRequest.setRole(Constants.USER_ROLE_PLAYER.getValue()); // Assuming all signups are for players
        userRequest.setUsername(signUpRequest.getMobile());
        return userRequest;
    }

}
