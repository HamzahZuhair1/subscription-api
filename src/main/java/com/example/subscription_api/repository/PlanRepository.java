package com.example.subscription_api.repository;

import com.example.subscription_api.entity.Plan;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, String> {
    @Transactional
    int deleteByIdEquals(String id);
}