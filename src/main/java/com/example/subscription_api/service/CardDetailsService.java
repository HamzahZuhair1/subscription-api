package com.example.subscription_api.service;

import com.example.subscription_api.dto.cards_details.CardsDetailsRequestDTO;
import com.example.subscription_api.dto.cards_details.CardsDetailsResponseDTO;
import com.example.subscription_api.entity.CardsDetails;
import com.example.subscription_api.repository.CardsDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardDetailsService {

    private final CardsDetailsRepository cardsDetailsRepository;

    public CardsDetailsResponseDTO createCardDetails(CardsDetailsRequestDTO requestDTO) {
        CardsDetails cardDetails = CardsDetails.builder()
                .cardHolderName(requestDTO.getCardHolderName())
                .cardBrand(requestDTO.getCardBrand())
                .lastFourDigits(requestDTO.getLastFourDigits())
                .isDefault(requestDTO.getIsDefault())
                .build();

        CardsDetails savedCard = cardsDetailsRepository.save(cardDetails);
        return mapToResponseDTO(savedCard);
    }

    public CardsDetailsResponseDTO getCardDetailsById(String id) {
        CardsDetails cardDetails = cardsDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card details not found with id: " + id));
        return mapToResponseDTO(cardDetails);
    }

    public List<CardsDetailsResponseDTO> getAllCardDetails() {
        return cardsDetailsRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public CardsDetailsResponseDTO updateCardDetails(String id, CardsDetailsRequestDTO requestDTO) {
        CardsDetails existingCard = cardsDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card details not found with id: " + id));

        existingCard.setCardHolderName(requestDTO.getCardHolderName());
        existingCard.setCardBrand(requestDTO.getCardBrand());
        existingCard.setLastFourDigits(requestDTO.getLastFourDigits());
        existingCard.setDefault(requestDTO.getIsDefault());

        CardsDetails updatedCard = cardsDetailsRepository.save(existingCard);
        return mapToResponseDTO(updatedCard);
    }

    public void deleteCardDetails(String id) {
        CardsDetails existingCard = cardsDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card details not found with id: " + id));
        cardsDetailsRepository.delete(existingCard);
    }

    private CardsDetailsResponseDTO mapToResponseDTO(CardsDetails cardDetails) {
        return CardsDetailsResponseDTO.builder()
                .id(cardDetails.getId())
                .cardHolderName(cardDetails.getCardHolderName())
                .cardBrand(cardDetails.getCardBrand())
                .lastFourDigits(cardDetails.getLastFourDigits())
                .isDefault(cardDetails.isDefault())
                .build();
    }
}