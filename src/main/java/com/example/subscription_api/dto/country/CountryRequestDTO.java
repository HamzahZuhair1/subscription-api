package com.example.subscription_api.dto.country;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryRequestDTO {

    @NotBlank(message = "Country code is required (e.g., JO, SA)")
    private String code;

    @NotBlank(message = "Country name is required")
    private String name;
}