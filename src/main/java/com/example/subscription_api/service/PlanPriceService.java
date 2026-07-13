package com.example.subscription_api.service;

import com.example.subscription_api.dto.plan_price.PlanPriceRequestDTO;
import com.example.subscription_api.dto.plan_price.PlanPriceResponseDTO;
import com.example.subscription_api.entity.Country;
import com.example.subscription_api.entity.Plan;
import com.example.subscription_api.entity.PlanPrice;
import com.example.subscription_api.exception.ResourceNotFoundException;
import com.example.subscription_api.mapper.PlanPriceMapper;
import com.example.subscription_api.repository.CountryRepository;
import com.example.subscription_api.repository.PlanPriceRepository;
import com.example.subscription_api.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanPriceService {

    private final PlanPriceRepository planPriceRepository;
    private final PlanRepository planRepository;
    private final CountryRepository countryRepository;
    private final PlanPriceMapper planPriceMapper;

    public PlanPriceResponseDTO createPlanPrice(PlanPriceRequestDTO requestDTO) {
        Plan plan = planRepository.findById(requestDTO.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + requestDTO.getPlanId()));

        Country country = countryRepository.findById(requestDTO.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + requestDTO.getCountryId()));

        PlanPrice planPrice = PlanPrice.builder()
                .plan(plan)
                .country(country)
                .cycleLength(requestDTO.getCycleLength())
                .cycleUnit(requestDTO.getCycleUnit())
                .amount(requestDTO.getAmount())
                .currency(requestDTO.getCurrency())
                .isActive(requestDTO.getIsActive())
                .build();

        PlanPrice savedPrice = planPriceRepository.save(planPrice);
        return planPriceMapper.toResponseDTO(savedPrice);
    }

    public PlanPriceResponseDTO getPlanPriceById(String id) {
        PlanPrice planPrice = planPriceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan price not found with id: " + id));
        return planPriceMapper.toResponseDTO(planPrice);
    }

    public Page<PlanPriceResponseDTO> getAllPlanPrices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return planPriceRepository.findAll(pageable)
                .map(planPriceMapper::toResponseDTO);
    }

    public PlanPriceResponseDTO updatePlanPrice(String id, PlanPriceRequestDTO requestDTO) {
        PlanPrice existingPrice = planPriceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan price not found with id: " + id));

        Plan plan = planRepository.findById(requestDTO.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + requestDTO.getPlanId()));
        Country country = countryRepository.findById(requestDTO.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + requestDTO.getCountryId()));

        existingPrice.setPlan(plan);
        existingPrice.setCountry(country);
        existingPrice.setCycleLength(requestDTO.getCycleLength());
        existingPrice.setCycleUnit(requestDTO.getCycleUnit());
        existingPrice.setAmount(requestDTO.getAmount());
        existingPrice.setCurrency(requestDTO.getCurrency());
        existingPrice.setActive(requestDTO.getIsActive());

        PlanPrice updatedPrice = planPriceRepository.save(existingPrice);
        return planPriceMapper.toResponseDTO(updatedPrice);
    }

    public void deletePlanPrice(String id) {
        if (planPriceRepository.deleteByIdEquals(id) == 0) {
            throw new ResourceNotFoundException("Plan price not found with id: " + id);
        }
    }


}