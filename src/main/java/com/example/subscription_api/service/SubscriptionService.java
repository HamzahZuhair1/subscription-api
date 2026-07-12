package com.example.subscription_api.service;

import com.example.subscription_api.dto.subscription.SubscriptionRequestDTO;
import com.example.subscription_api.dto.subscription.SubscriptionResponseDTO;
import com.example.subscription_api.entity.CardsDetails;
import com.example.subscription_api.entity.PlanPrice;
import com.example.subscription_api.entity.Subscription;
import com.example.subscription_api.entity.User;
import com.example.subscription_api.enums.SubscriptionStatus;
import com.example.subscription_api.exception.ResourceNotFoundException;
import com.example.subscription_api.mapper.SubscriptionMapper;
import com.example.subscription_api.repository.CardsDetailsRepository;
import com.example.subscription_api.repository.PlanPriceRepository;
import com.example.subscription_api.repository.SubscriptionRepository;
import com.example.subscription_api.repository.UserRepository;
import com.example.subscription_api.util.DateTimeUtil; // <-- استدعاء الـ Util
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final SubscriptionMapper subscriptionMapper;
    private final DateTimeUtil dateTimeUtil;

    public Page<SubscriptionResponseDTO> getAllSubscriptions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return subscriptionRepository.findAll(pageable).map(subscriptionMapper::toResponseDTO);
    }

    public SubscriptionResponseDTO getSubscriptionById(String id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id: " + id));
        return subscriptionMapper.toResponseDTO(subscription);
    }

    public List<SubscriptionResponseDTO> getUserSubscriptions(String userId) {
        checkUserExists(userId);
        return subscriptionRepository.findByUserId(userId).stream()
                .map(subscriptionMapper::toResponseDTO)
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

        LocalDateTime now = dateTimeUtil.now();

        Subscription subscription = Subscription.builder()
                .user(user)
                .planPrice(planPrice)
                .cardsDetails(cardsDetails)
                .autoRenew(requestDTO.getAutoRenew())
                .status(SubscriptionStatus.ACTIVE)
                .startDate(now)
                .endDate(dateTimeUtil.calculateEndDate(now, planPrice.getCycleLength(), planPrice.getCycleUnit().getDaysValue()))
                .build();

        return subscriptionMapper.toResponseDTO(subscriptionRepository.save(subscription));
    }

    public List<SubscriptionResponseDTO> getInActiveSubscriptions(String userId) {
        checkUserExists(userId);
        return subscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.INACTIVE).stream()
                .map(subscriptionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void renewSubscription(String userId, String subId) {
        checkUserExists(userId);
        Subscription targetSub = getValidUserSubscription(userId, subId);

        targetSub.setAutoRenew(true);
        targetSub.setStatus(SubscriptionStatus.ACTIVE);

        LocalDateTime baseDate = dateTimeUtil.getRenewalBaseDate(targetSub.getEndDate());
        targetSub.setEndDate(dateTimeUtil.calculateEndDate(baseDate, targetSub.getPlanPrice().getCycleLength(), targetSub.getPlanPrice().getCycleUnit().getDaysValue()));

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

    @Transactional
    public void processExpiredSubscriptions() {
        LocalDateTime now = dateTimeUtil.now();
        List<Subscription> expiredSubscriptions = subscriptionRepository.findByStatusAndEndDateBefore(SubscriptionStatus.ACTIVE, now);

        for (Subscription sub : expiredSubscriptions) {
            if (sub.isAutoRenew()) {
                try {
                    renewSubscription(sub.getUser().getId(), sub.getId());
                } catch (Exception e) {
                    sub.setStatus(SubscriptionStatus.INACTIVE);
                    subscriptionRepository.save(sub);
                }
            } else {
                sub.setStatus(SubscriptionStatus.EXPIRED);
                subscriptionRepository.save(sub);
            }
        }
    }
}