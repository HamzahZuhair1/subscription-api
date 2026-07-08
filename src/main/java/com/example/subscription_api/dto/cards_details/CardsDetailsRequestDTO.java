package com.example.subscription_api.dto.cards_details;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(min = 4, max = 4, message = "Must be exactly 4 digits")
    private String lastFourDigits;

    @NotBlank(message = "Payment token is required")
    private String paymentToken;

    @NotNull(message = "Default status must be specified")
    private Boolean isDefault;
}