package com.example.subscription_api.repository;

import com.example.subscription_api.entity.PlanPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanPriceRepository extends JpaRepository<PlanPrice, String> {
}