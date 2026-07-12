package com.example.subscription_api.dto.plan_price;

import com.example.subscription_api.enums.CycleUnit;
import com.example.subscription_api.validation.ValidCurrency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.example.subscription_api.enums.CycleUnit;

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

    @NotNull(message = "Cycle unit is required")
    private CycleUnit cycleUnit;

    @Min(value = 0, message = "Amount cannot be negative")
    private long amount;

    @ValidCurrency
    private String currency;

    @NotNull(message = "Active status must be specified")
    @Builder.Default
    private Boolean isActive = true;
}