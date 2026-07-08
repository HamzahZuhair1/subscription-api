package com.example.subscription_api.dto.country;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryResponseDTO {
    private String id;
    private String code;
    private String name;
}