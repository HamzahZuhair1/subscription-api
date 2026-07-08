package com.example.subscription_api.dto.cards_details;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardsDetailsResponseDTO {
    private String id;
    private String cardHolderName;
    private String cardBrand;
    private String lastFourDigits;
    private boolean isDefault;
}