package com.cricket.mpl.service.impl;

import com.cricket.mpl.dto.request.UserRequest;
import com.cricket.mpl.dto.response.UserDetailsResponse;
import com.cricket.mpl.entity.Team;
import com.cricket.mpl.entity.User;
import com.cricket.mpl.repository.TeamRepository;
import com.cricket.mpl.repository.UserRepository;
import com.cricket.mpl.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    public UserServiceImpl(UserRepository userRepository, TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }


    @Override
    public User addUser(UserRequest userRequest) {
        return userRepository.save(userRequest.toEntity());
    }

    @Override
    public UserDetailsResponse getUserDetails(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        User userObj = user.get();
        Team team = teamRepository.findByCaptionUserId(userId);

        if(team!=null){
            return UserDetailsResponse.builder()
                    .userRole(userObj.getRole())
                    .name(userObj.getName())
                    .team(team.getTeamName())
                    .teamStatus(team.getTeamStatus())
                    .teamId(team.getId())
                    .userId(userObj.getUserId()).build();
        }

        return  UserDetailsResponse.builder()
                .userRole(userObj.getRole())
                .name(userObj.getName())
                .userId(userObj.getUserId()).build();
    }
}
