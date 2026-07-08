package com.example.subscription_api.dto.user;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private LocalDateTime createdAt;
}