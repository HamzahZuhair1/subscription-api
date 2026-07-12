package com.example.subscription_api.controller;

import com.example.subscription_api.dto.cards_details.CardsDetailsRequestDTO;
import com.example.subscription_api.dto.cards_details.CardsDetailsResponseDTO;
import com.example.subscription_api.service.CardDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users/{userId}/cards-details")
@RequiredArgsConstructor
public class CardDetailsController {

    private final CardDetailsService cardDetailsService;

    @GetMapping
    public ResponseEntity<List<CardsDetailsResponseDTO>> getUserCards(@PathVariable String userId) {
        return ResponseEntity.ok(cardDetailsService.getAllCardsByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<CardsDetailsResponseDTO> createCard(
            @PathVariable String userId,
            @Valid @RequestBody CardsDetailsRequestDTO requestDTO) {

        return new ResponseEntity<>(cardDetailsService.createCard(userId, requestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{cardId}")
    public ResponseEntity<CardsDetailsResponseDTO> updateCard(
            @PathVariable String userId,
            @PathVariable String cardId,
            @Valid @RequestBody CardsDetailsRequestDTO requestDTO) {

        return ResponseEntity.ok(cardDetailsService.updateCard(userId, cardId, requestDTO));
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(
            @PathVariable String userId,
            @PathVariable String cardId) {

        cardDetailsService.deleteCard(userId, cardId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{cardId}/default")
    public ResponseEntity<Void> setDefaultCard(
            @PathVariable String userId,
            @PathVariable String cardId) {

        cardDetailsService.setDefaultCard(userId, cardId);
        return ResponseEntity.ok().build();
    }
}