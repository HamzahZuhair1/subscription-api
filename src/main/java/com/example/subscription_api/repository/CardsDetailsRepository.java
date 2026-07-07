package com.example.subscription_api.repository;

import com.example.subscription_api.entity.CardsDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardsDetailsRepository extends JpaRepository<CardsDetails, String> {
    List<CardsDetails> findByUserId(String userId);
}