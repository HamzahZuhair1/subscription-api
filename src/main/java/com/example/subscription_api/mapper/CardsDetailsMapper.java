package com.example.subscription_api.mapper;

import com.example.subscription_api.dto.cards_details.CardsDetailsResponseDTO;
import com.example.subscription_api.entity.CardsDetails;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardsDetailsMapper {

    CardsDetailsResponseDTO toResponseDTO(CardsDetails cardsDetails);

}