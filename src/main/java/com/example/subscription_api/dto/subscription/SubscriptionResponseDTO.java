package com.example.subscription_api.dto.subscription;

import com.example.subscription_api.enums.SubscriptionStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionResponseDTO {
    private String id;
    private String userId;
    private String planPriceId;
    private String cardDetailsId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private SubscriptionStatus status;
    private boolean autoRenew;
}
