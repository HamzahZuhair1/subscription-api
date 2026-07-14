package com.example.subscription_api.controller;

import com.example.subscription_api.dto.cards_details.CardsDetailsRequestDTO;
import com.example.subscription_api.dto.cards_details.CardsDetailsResponseDTO;
import com.example.subscription_api.dto.subscription.SubscriptionRequestDTO;
import com.example.subscription_api.dto.subscription.SubscriptionResponseDTO;
import com.example.subscription_api.dto.user.UserRequestDTO;
import com.example.subscription_api.dto.user.UserResponseDTO;
import com.example.subscription_api.service.CardDetailsService;
import com.example.subscription_api.service.SubscriptionService;
import com.example.subscription_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO requestDTO) {
        return new ResponseEntity<>(userService.createUser(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable String id, @RequestBody UserRequestDTO requestDTO) {
        return ResponseEntity.ok(userService.updateUser(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    //CardDetailsController

    private final CardDetailsService cardDetailsService;

    @GetMapping("{userId}/cards-details")
    public ResponseEntity<List<CardsDetailsResponseDTO>> getUserCards(@PathVariable String userId) {
        return ResponseEntity.ok(cardDetailsService.getAllCardsByUserId(userId));
    }

    @PostMapping("{userId}/cards-details")
    public ResponseEntity<CardsDetailsResponseDTO> createCard(
            @PathVariable String userId,
            @Valid @RequestBody CardsDetailsRequestDTO requestDTO) {

        return new ResponseEntity<>(cardDetailsService.createCard(userId, requestDTO), HttpStatus.CREATED);
    }

    @PutMapping("{userId}/cards-details/{cardId}")
    public ResponseEntity<CardsDetailsResponseDTO> updateCard(
            @PathVariable String userId,
            @PathVariable String cardId,
            @Valid @RequestBody CardsDetailsRequestDTO requestDTO) {

        return ResponseEntity.ok(cardDetailsService.updateCard(userId, cardId, requestDTO));
    }

    @DeleteMapping("{userId}/cards-details/{cardId}")
    public ResponseEntity<Void> deleteCard(
            @PathVariable String userId,
            @PathVariable String cardId) {

        cardDetailsService.deleteCard(userId, cardId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/cards-details/{cardId}/default")
    public ResponseEntity<Void> setDefaultCard(
            @PathVariable String userId,
            @PathVariable String cardId) {

        cardDetailsService.setDefaultCard(userId, cardId);
        return ResponseEntity.ok().build();
    }


    //SubscriptionController

    private final SubscriptionService subscriptionService;

    @GetMapping("/{userId}/subscriptions")
    public ResponseEntity<List<SubscriptionResponseDTO>> getUserSubscriptions(@PathVariable String userId) {
        return ResponseEntity.ok(subscriptionService.getUserSubscriptions(userId));
    }

    @PostMapping("/{userId}/subscriptions")
    public ResponseEntity<SubscriptionResponseDTO> createSubscription(
            @PathVariable String userId,
            @Valid @RequestBody SubscriptionRequestDTO requestDTO) {
        return new ResponseEntity<>(subscriptionService.createSubscription(userId, requestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/subscriptions/inActive")
    public ResponseEntity<List<SubscriptionResponseDTO>> getInActiveSubscriptions(@PathVariable String userId) {
        return ResponseEntity.ok(subscriptionService.getInActiveSubscriptions(userId));
    }

    @PatchMapping("/{userId}/subscriptions/{subId}/reNew")
    public ResponseEntity<Void> renewSubscription(
            @PathVariable String userId,
            @PathVariable String subId) {
        subscriptionService.renewSubscription(userId, subId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}/subscriptions/{subId}/cancel")
    public ResponseEntity<Void> cancelSubscription(
            @PathVariable String userId,
            @PathVariable String subId) {
        subscriptionService.cancelSubscription(userId, subId);
        return ResponseEntity.ok().build();
    }
}