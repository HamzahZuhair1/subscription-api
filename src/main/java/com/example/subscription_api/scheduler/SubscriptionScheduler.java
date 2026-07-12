package com.example.subscription_api.scheduler;

import com.example.subscription_api.entity.Subscription;
import com.example.subscription_api.enums.SubscriptionStatus;
import com.example.subscription_api.repository.SubscriptionRepository;
import com.example.subscription_api.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionScheduler {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionService subscriptionService;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void processExpiredSubscriptions() {
        log.info("Starting scheduled task: Checking for expired subscriptions...");

        LocalDateTime now = LocalDateTime.now();
        List<Subscription> expiredSubscriptions = subscriptionRepository.findByStatusAndEndDateBefore(SubscriptionStatus.ACTIVE, now);

        int renewedCount = 0;
        int expiredCount = 0;

        for (Subscription sub : expiredSubscriptions) {
            if (sub.isAutoRenew()) {
                try {
                    subscriptionService.renewSubscription(sub.getUser().getId(), sub.getId());
                    renewedCount++;
                    log.info("Successfully renewed subscription ID: {}", sub.getId());
                } catch (Exception e) {
                    sub.setStatus(SubscriptionStatus.INACTIVE);
                    subscriptionRepository.save(sub);
                    log.error("Failed to renew subscription ID: {}. Status changed to INACTIVE.", sub.getId(), e);
                }
            } else {
                sub.setStatus(SubscriptionStatus.EXPIRED);
                subscriptionRepository.save(sub);
                expiredCount++;
                log.info("Subscription ID: {} expired.", sub.getId());
            }
        }

        log.info("Scheduled task finished. Renewed: {}, Expired: {}", renewedCount, expiredCount);
    }
}