package com.example.subscription_api.util;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class DateTimeUtil {

    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    public LocalDateTime calculateEndDate(LocalDateTime startDate, int cycleLength, int cycleUnitDays) {
        int totalDays = cycleLength * cycleUnitDays;
        return startDate.plusDays(totalDays);
    }

    public boolean isExpired(LocalDateTime endDate) {
        return endDate.isBefore(now());
    }

    public LocalDateTime getRenewalBaseDate(LocalDateTime currentEndDate) {
        return isExpired(currentEndDate) ? now() : currentEndDate;
    }
}