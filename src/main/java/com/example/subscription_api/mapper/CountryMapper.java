package com.example.subscription_api.mapper;

import com.example.subscription_api.dto.country.CountryResponseDTO;
import com.example.subscription_api.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CountryMapper {

    CountryResponseDTO toResponseDTO(Country country);

}