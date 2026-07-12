package com.example.subscription_api.mapper;

import com.example.subscription_api.dto.plan_price.PlanPriceResponseDTO;
import com.example.subscription_api.entity.PlanPrice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlanPriceMapper {

    @Mapping(source = "plan.id", target = "planId")
    @Mapping(source = "country.id", target = "countryId")
    PlanPriceResponseDTO toResponseDTO(PlanPrice planPrice);

}