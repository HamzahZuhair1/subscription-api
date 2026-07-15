package com.example.subscription_api.dto.cards_details;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("isDefault")
    private boolean isDefault;
}