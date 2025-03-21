package com.example.skystayback.repositories;

import com.example.skystayback.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    Optional<User> findTopByEmail(String email);
    Optional<User> findFirstByEmail(String email);
    boolean existsByUserCode(String userCode);
    boolean existsByEmail(String email);
    boolean existsByNif(String nif);
    boolean existsByPhone(String phone);
}
