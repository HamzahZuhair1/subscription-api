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
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardDetailsController {

    private final CardDetailsService cardDetailsService;

    @PostMapping
    public ResponseEntity<CardsDetailsResponseDTO> createCardDetails(@Valid @RequestBody CardsDetailsRequestDTO requestDTO) {
        CardsDetailsResponseDTO createdCard = cardDetailsService.createCardDetails(requestDTO);
        return new ResponseEntity<>(createdCard, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CardsDetailsResponseDTO>> getAllCardDetails() {
        return ResponseEntity.ok(cardDetailsService.getAllCardDetails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardsDetailsResponseDTO> getCardDetailsById(@PathVariable String id) {
        return ResponseEntity.ok(cardDetailsService.getCardDetailsById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardsDetailsResponseDTO> updateCardDetails(@PathVariable String id, @Valid @RequestBody CardsDetailsRequestDTO requestDTO) {
        return ResponseEntity.ok(cardDetailsService.updateCardDetails(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCardDetails(@PathVariable String id) {
        cardDetailsService.deleteCardDetails(id);
        return ResponseEntity.noContent().build();
    }
}