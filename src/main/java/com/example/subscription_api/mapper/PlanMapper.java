package com.example.subscription_api.mapper;

import com.example.subscription_api.dto.plan.PlanResponseDTO;
import com.example.subscription_api.entity.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlanMapper {

    PlanResponseDTO toResponseDTO(Plan plan);

}