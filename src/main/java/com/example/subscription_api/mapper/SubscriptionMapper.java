package com.example.subscription_api.mapper;

import com.example.subscription_api.dto.subscription.SubscriptionResponseDTO;
import com.example.subscription_api.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubscriptionMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "planPrice.id", target = "planPriceId")
    @Mapping(source = "cardsDetails.id", target = "cardDetailsId")
    SubscriptionResponseDTO toResponseDTO(Subscription subscription);
}