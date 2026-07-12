package com.example.subscription_api.service;

import com.example.subscription_api.dto.subscription.SubscriptionRequestDTO;
import com.example.subscription_api.dto.subscription.SubscriptionResponseDTO;
import com.example.subscription_api.entity.CardsDetails;
import com.example.subscription_api.entity.PlanPrice;
import com.example.subscription_api.entity.Subscription;
import com.example.subscription_api.entity.User;
import com.example.subscription_api.enums.SubscriptionStatus;
import com.example.subscription_api.exception.ResourceNotFoundException;
import com.example.subscription_api.repository.CardsDetailsRepository;
import com.example.subscription_api.repository.PlanPriceRepository;
import com.example.subscription_api.repository.SubscriptionRepository;
import com.example.subscription_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<SubscriptionResponseDTO> getAllSubscriptions() {
        return subscriptionRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public SubscriptionResponseDTO getSubscriptionById(String id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id: " + id));
        return mapToResponseDTO(subscription);
    }

    public List<SubscriptionResponseDTO> getUserSubscriptions(String userId) {
        checkUserExists(userId);
        return subscriptionRepository.findByUserId(userId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SubscriptionResponseDTO createSubscription(String userId, SubscriptionRequestDTO requestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        PlanPrice planPrice = planPriceRepository.findById(requestDTO.getPlanPriceId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan Price not found"));

        CardsDetails cardsDetails = cardsDetailsRepository.findById(requestDTO.getCardDetailsId())
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));

        if (!cardsDetails.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Card does not belong to this user");
        }

        LocalDateTime now = LocalDateTime.now();

        Subscription subscription = Subscription.builder()
                .user(user)
                .planPrice(planPrice)
                .cardsDetails(cardsDetails)
                .autoRenew(requestDTO.getAutoRenew())
                .status(SubscriptionStatus.ACTIVE)
                .startDate(now)
                .endDate(calculateEndDate(now, planPrice))
                .build();

        return mapToResponseDTO(subscriptionRepository.save(subscription));
    }

    public List<SubscriptionResponseDTO> getInActiveSubscriptions(String userId) {
        checkUserExists(userId);
        return subscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.INACTIVE).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void renewSubscription(String userId, String subId) {
        checkUserExists(userId);
        Subscription targetSub = getValidUserSubscription(userId, subId);

        targetSub.setAutoRenew(true);
        targetSub.setStatus(SubscriptionStatus.ACTIVE);

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime baseDate = targetSub.getEndDate().isAfter(now) ? targetSub.getEndDate() : now;

        targetSub.setEndDate(calculateEndDate(baseDate, targetSub.getPlanPrice()));

        subscriptionRepository.save(targetSub);
    }

    @Transactional
    public void cancelSubscription(String userId, String subId) {
        checkUserExists(userId);
        Subscription targetSub = getValidUserSubscription(userId, subId);

        targetSub.setAutoRenew(false);
        targetSub.setStatus(SubscriptionStatus.CANCELED);

        subscriptionRepository.save(targetSub);
    }

    private LocalDateTime calculateEndDate(LocalDateTime startDate, PlanPrice planPrice) {
        int totalDays = planPrice.getCycleLength() * planPrice.getCycleUnit().getDaysValue();
        return startDate.plusDays(totalDays);
    }

    private void checkUserExists(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
    }

    private Subscription getValidUserSubscription(String userId, String subId) {
        Subscription subscription = subscriptionRepository.findById(subId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));
        if (!subscription.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Subscription does not belong to this user");
        }
        return subscription;
    }

    private SubscriptionResponseDTO mapToResponseDTO(Subscription subscription) {
        return SubscriptionResponseDTO.builder()
                .id(subscription.getId())
                .userId(subscription.getUser().getId())
                .planPriceId(subscription.getPlanPrice().getId())
                .cardDetailsId(subscription.getCardsDetails().getId())
                .status(subscription.getStatus())
                .autoRenew(subscription.isAutoRenew())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .build();
    }
}