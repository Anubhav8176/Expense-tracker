package com.anucodes.expensetracker.repository;

import com.anucodes.expensetracker.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> getUserInfoByUsername(String username);
    UserInfo findByUsername(String username);
}
