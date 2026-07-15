package com.example.subscription_api.dto.plan;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private boolean active;
}
