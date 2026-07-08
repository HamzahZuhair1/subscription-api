package com.example.subscription_api.dto.plan_price;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanPriceRequestDTO {

    @NotBlank(message = "Plan ID is required")
    private String planId;

    @NotBlank(message = "Country ID is required")
    private String countryId;

    @Min(value = 1, message = "Cycle length must be at least 1")
    private int cycleLength;

    @NotBlank(message = "Cycle unit is required (e.g., MONTH, YEAR)")
    private String cycleUnit;

    @Min(value = 0, message = "Amount cannot be negative")
    private long amount;

    @NotBlank(message = "Currency is required (e.g., JOD, USD)")
    private String currency;

    @NotNull(message = "Active status must be specified")
    private Boolean isActive;
}