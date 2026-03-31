package com.cricket.mpl.repository;

import com.cricket.mpl.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Integer> {

    Player findByUserId(Integer captionUserId);

    List<Player> findByTeamId(Integer id);
}
