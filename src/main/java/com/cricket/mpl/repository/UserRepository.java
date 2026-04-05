package com.cricket.mpl.repository;

import com.cricket.mpl.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByUsernameAndPassword(String mobile, String password);
    User findByUsername(String username);
}
