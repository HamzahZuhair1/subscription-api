package com.example.subscription_api.repository;

import com.example.subscription_api.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, String> {
    List<Subscription> findByUserId(String userId);
    List<Subscription> findByUserIdAndStatus(String userId, String status);
}