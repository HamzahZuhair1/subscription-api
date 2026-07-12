package com.example.subscription_api.dto.plan_price;

import com.example.subscription_api.enums.CycleUnit;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanPriceResponseDTO {
    private String id;
    private String planId;
    private String countryId;
    private int cycleLength;
    private CycleUnit cycleUnit;
    private long amount;
    private String currency;
    private boolean isActive;
}