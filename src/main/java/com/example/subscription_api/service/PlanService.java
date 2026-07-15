package com.example.subscription_api.service;

import com.example.subscription_api.dto.plan.PlanRequestDTO;
import com.example.subscription_api.dto.plan.PlanResponseDTO;
import com.example.subscription_api.entity.Plan;
import com.example.subscription_api.exception.ResourceNotFoundException;
import com.example.subscription_api.mapper.PlanMapper;
import com.example.subscription_api.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final PlanMapper planMapper;

    public PlanResponseDTO createPlan(PlanRequestDTO requestDTO) {
        Plan plan = Plan.builder()
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .active(requestDTO.getActive())
                .build();

        Plan savedPlan = planRepository.save(plan);

        PlanResponseDTO dto = planMapper.toResponseDTO(savedPlan);

        System.out.println("Entity active = " + savedPlan.isActive());
        System.out.println("DTO active = " + dto.isActive());

        return dto;

//        return planMapper.toResponseDTO(savedPlan);
    }

    public List<PlanResponseDTO> getAllPlans() {
        return planRepository.findAll()
                .stream()
                .map(planMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public PlanResponseDTO getPlanById(String id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + id));
        return planMapper.toResponseDTO(plan);
    }

    public PlanResponseDTO updatePlan(String id, PlanRequestDTO requestDTO) {
        Plan existingPlan = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + id));

        existingPlan.setName(requestDTO.getName());
        existingPlan.setDescription(requestDTO.getDescription());
        existingPlan.setActive(requestDTO.getActive());

        Plan updatedPlan = planRepository.save(existingPlan);
        return planMapper.toResponseDTO(updatedPlan);
    }

    public void deletePlan(String id) {
        if (planRepository.deleteByIdEquals(id) == 0) {
            throw new ResourceNotFoundException("Plan not found with id: " + id);
        }
    }

}