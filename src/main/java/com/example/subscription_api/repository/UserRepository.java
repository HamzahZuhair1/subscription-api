package com.example.subscription_api.repository;

import com.example.subscription_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
    boolean existsByMobileNumber(String mobileNumber);
    boolean existsByEmailAndIdNot(String email, String id);
    boolean existsByMobileNumberAndIdNot(String mobileNumber, String id);
    Optional<User> findFirstByEmailOrMobileNumber(String email, String mobileNumber);
}