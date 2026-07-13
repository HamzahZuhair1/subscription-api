package com.example.subscription_api.repository;

import com.example.subscription_api.entity.Country;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {
    @Transactional
    int deleteByIdEquals(String id);
}