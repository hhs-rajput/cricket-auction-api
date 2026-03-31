package com.cricket.mpl.controller;

import com.cricket.mpl.dto.request.UserRequest;
import com.cricket.mpl.dto.response.UserDetailsResponse;
import com.cricket.mpl.service.PlayerService;
import com.cricket.mpl.service.UserService;
import com.cricket.mpl.util.Constants;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    private final PlayerService playerService;

    public UserController(UserService userService, PlayerService playerService) {
        this.userService = userService;
        this.playerService = playerService;
    }

    @GetMapping("/{userId}/details")
    public UserDetailsResponse getUserDetails(@PathVariable Integer userId) {
        return userService.getUserDetails(userId);
    }

    @PostMapping
    public String addUser(@RequestBody UserRequest userRequest) {
        userRequest.setRole(Constants.USER_ROLE_PLAYER.getValue());
        userService.addUser(userRequest);
        //playerService.addPlayer();
        // In a real application, you'd save the player to the database here
        return "User " + userRequest.getName() + " added successfully!";
    }
}
