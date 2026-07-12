package com.example.subscription_api.scheduler;

import com.example.subscription_api.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionScheduler {

    private final SubscriptionService subscriptionService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void processExpiredSubscriptions() {
        subscriptionService.processExpiredSubscriptions();
    }
}