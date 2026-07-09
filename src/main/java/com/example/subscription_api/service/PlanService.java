package com.example.subscription_api.service;

import com.example.subscription_api.dto.plan.PlanRequestDTO;
import com.example.subscription_api.dto.plan.PlanResponseDTO;
import com.example.subscription_api.entity.Plan;
import com.example.subscription_api.exception.ResourceNotFoundException;
import com.example.subscription_api.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;

    public PlanResponseDTO createPlan(PlanRequestDTO requestDTO) {
        Plan plan = Plan.builder()
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .isActive(requestDTO.getIsActive())
                .build();

        Plan savedPlan = planRepository.save(plan);
        return mapToResponseDTO(savedPlan);
    }

    public List<PlanResponseDTO> getAllPlans() {
        return planRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public PlanResponseDTO getPlanById(String id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + id));
        return mapToResponseDTO(plan);
    }

    public PlanResponseDTO updatePlan(String id, PlanRequestDTO requestDTO) {
        Plan existingPlan = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + id));

        existingPlan.setName(requestDTO.getName());
        existingPlan.setDescription(requestDTO.getDescription());
        existingPlan.setActive(requestDTO.getIsActive());

        Plan updatedPlan = planRepository.save(existingPlan);
        return mapToResponseDTO(updatedPlan);
    }

    public void deletePlan(String id) {
        Plan existingPlan = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + id));
        planRepository.delete(existingPlan);
    }

    private PlanResponseDTO mapToResponseDTO(Plan plan) {
        return PlanResponseDTO.builder()
                .id(plan.getId())
                .name(plan.getName())
                .description(plan.getDescription())
                .isActive(plan.isActive())
                .build();
    }
}