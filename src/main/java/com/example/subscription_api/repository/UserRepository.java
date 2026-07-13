package com.example.subscription_api.repository;

import com.example.subscription_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findFirstByEmailOrMobileNumber(String email, String mobileNumber);
    @Transactional
    int deleteByIdEquals(String id);
}