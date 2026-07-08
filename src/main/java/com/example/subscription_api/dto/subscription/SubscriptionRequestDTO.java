package com.example.subscription_api.dto.subscription;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionRequestDTO {

    @NotBlank(message = "Plan Price ID is required")
    private String planPriceId;

    @NotBlank(message = "Card Details ID is required")
    private String cardDetailsId;

    @NotNull(message = "Auto renew preference must be specified")
    private Boolean autoRenew;
}