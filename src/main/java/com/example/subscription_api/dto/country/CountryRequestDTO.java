package com.example.subscription_api.dto.country;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryRequestDTO {

    @NotBlank(message = "Country code is required (e.g., JO, SA)")
    @Size(min = 2, max = 3, message = "The code must be exactly 2 or 3 characters long")
    private String code;

    @NotBlank(message = "Country name is required")
    private String name;
}