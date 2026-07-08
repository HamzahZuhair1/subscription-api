package com.example.subscription_api.service;

import com.example.subscription_api.dto.subscription.SubscriptionRequestDTO;
import com.example.subscription_api.dto.subscription.SubscriptionResponseDTO;
import com.example.subscription_api.entity.CardsDetails;
import com.example.subscription_api.entity.PlanPrice;
import com.example.subscription_api.entity.Subscription;
import com.example.subscription_api.entity.User;
import com.example.subscription_api.repository.CardsDetailsRepository;
import com.example.subscription_api.repository.PlanPriceRepository;
import com.example.subscription_api.repository.SubscriptionRepository;
import com.example.subscription_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final PlanPriceRepository planPriceRepository;
    private final CardsDetailsRepository cardsDetailsRepository;

    public SubscriptionResponseDTO createSubscription(SubscriptionRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + requestDTO.getUserId()));

        PlanPrice planPrice = planPriceRepository.findById(requestDTO.getPlanPriceId())
                .orElseThrow(() -> new RuntimeException("Plan Price not found with id: " + requestDTO.getPlanPriceId()));

        CardsDetails cardsDetails = cardsDetailsRepository.findById(requestDTO.getCardDetailsId())
                .orElseThrow(() -> new RuntimeException("Card Details not found with id: " + requestDTO.getCardDetailsId()));

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime calculatedEndDate = now;
        if ("MONTH".equalsIgnoreCase(planPrice.getCycleUnit())) {
            calculatedEndDate = now.plusMonths(planPrice.getCycleLength());
        } else if ("YEAR".equalsIgnoreCase(planPrice.getCycleUnit())) {
            calculatedEndDate = now.plusYears(planPrice.getCycleLength());
        } else if ("DAY".equalsIgnoreCase(planPrice.getCycleUnit())) {
            calculatedEndDate = now.plusDays(planPrice.getCycleLength());
        }

        Subscription subscription = Subscription.builder()
                .user(user)
                .planPrice(planPrice)
                .cardsDetails(cardsDetails)
                .startDate(now)
                .endDate(calculatedEndDate)
                .status("ACTIVE")
                .autoRenew(requestDTO.getAutoRenew())
                .build();

        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return mapToResponseDTO(savedSubscription);
    }

    public SubscriptionResponseDTO getSubscriptionById(String id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + id));
        return mapToResponseDTO(subscription);
    }

    public List<SubscriptionResponseDTO> getAllSubscriptions() {
        return subscriptionRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public SubscriptionResponseDTO updateSubscription(String id, SubscriptionRequestDTO requestDTO) {
        Subscription existingSubscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + id));

        CardsDetails cardsDetails = cardsDetailsRepository.findById(requestDTO.getCardDetailsId())
                .orElseThrow(() -> new RuntimeException("Card Details not found with id: " + requestDTO.getCardDetailsId()));

        existingSubscription.setCardsDetails(cardsDetails);
        existingSubscription.setAutoRenew(requestDTO.getAutoRenew());

        Subscription updatedSubscription = subscriptionRepository.save(existingSubscription);
        return mapToResponseDTO(updatedSubscription);
    }

    public void deleteSubscription(String id) {
        Subscription existingSubscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + id));
        subscriptionRepository.delete(existingSubscription);
    }

    private SubscriptionResponseDTO mapToResponseDTO(Subscription subscription) {
        return SubscriptionResponseDTO.builder()
                .id(subscription.getId())
                .userId(subscription.getUser().getId())
                .planPriceId(subscription.getPlanPrice().getId())
                .cardDetailsId(subscription.getCardsDetails().getId())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .status(subscription.getStatus())
                .autoRenew(subscription.isAutoRenew())
                .build();
    }
}