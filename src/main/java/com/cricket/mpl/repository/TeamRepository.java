package com.cricket.mpl.repository;

import com.cricket.mpl.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team,Integer> {
    Team findByCaptionUserId(Integer userId);

    List<Team> findByTeamStatus(String teamStatus);

    List<Team> findByIdIn(List<Integer> ids);
}
