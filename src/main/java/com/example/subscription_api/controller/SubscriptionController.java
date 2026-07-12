package com.example.subscription_api.controller;

import com.example.subscription_api.dto.subscription.SubscriptionRequestDTO;
import com.example.subscription_api.dto.subscription.SubscriptionResponseDTO;
import com.example.subscription_api.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/subscriptions")
    public ResponseEntity<Page<SubscriptionResponseDTO>> getAllSubscriptions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(subscriptionService.getAllSubscriptions(page, size));
    }

    @GetMapping("/subscriptions/{id}")
    public ResponseEntity<SubscriptionResponseDTO> getSubscriptionById(@PathVariable String id) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionById(id));
    }

    @GetMapping("/users/{userId}/subscriptions")
    public ResponseEntity<List<SubscriptionResponseDTO>> getUserSubscriptions(@PathVariable String userId) {
        return ResponseEntity.ok(subscriptionService.getUserSubscriptions(userId));
    }

    @PostMapping("/users/{userId}/subscriptions")
    public ResponseEntity<SubscriptionResponseDTO> createSubscription(
            @PathVariable String userId,
            @Valid @RequestBody SubscriptionRequestDTO requestDTO) {
        return new ResponseEntity<>(subscriptionService.createSubscription(userId, requestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/subscriptions/inActive")
    public ResponseEntity<List<SubscriptionResponseDTO>> getInActiveSubscriptions(@PathVariable String userId) {
        return ResponseEntity.ok(subscriptionService.getInActiveSubscriptions(userId));
    }

    @PatchMapping("/users/{userId}/subscriptions/{subId}/reNew")
    public ResponseEntity<Void> renewSubscription(
            @PathVariable String userId,
            @PathVariable String subId) {
        subscriptionService.renewSubscription(userId, subId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/users/{userId}/subscriptions/{subId}/cancel")
    public ResponseEntity<Void> cancelSubscription(
            @PathVariable String userId,
            @PathVariable String subId) {
        subscriptionService.cancelSubscription(userId, subId);
        return ResponseEntity.ok().build();
    }
}