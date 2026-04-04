package com.cricket.mpl.repository;

import com.cricket.mpl.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Integer> {

    Player findByUserId(Integer captionUserId);

    List<Player> findBySoldAndTeamId(Boolean sold,Integer id);

    //List<Player> findByPlayerCategory(String category);

    List<Player> findBySoldAndCaptionAndPlayerCategory(boolean sold, boolean caption, String category);

}
