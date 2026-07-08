package com.example.subscription_api.dto.plan;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanResponseDTO {
    private String id;
    private String name;
    private String description;
    private boolean isActive;
}
