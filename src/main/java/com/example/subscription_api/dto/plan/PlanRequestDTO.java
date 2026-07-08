package com.example.subscription_api.dto.plan;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanRequestDTO {

    @NotBlank(message = "Plan name is required")
    private String name;

    private String description;

    @NotNull(message = "Plan active status must be specified")
    private Boolean isActive;
}
