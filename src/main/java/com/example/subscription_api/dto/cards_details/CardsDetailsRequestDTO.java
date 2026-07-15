package com.example.subscription_api.dto.cards_details;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardsDetailsRequestDTO {

    @NotBlank(message = "Card holder name is required")
    private String cardHolderName;

    @NotBlank(message = "Card brand is required (e.g., Visa)")
    private String cardBrand;

    @NotBlank(message = "Last four digits are required")
    @Pattern(regexp = "^\\d{4}$", message = "Last four digits must be exactly 4 numeric digits")
    private String lastFourDigits;

    @NotNull(message = "Default status must be specified")
    @JsonProperty("isDefault")
    @Builder.Default
    private Boolean isDefault = false;
}