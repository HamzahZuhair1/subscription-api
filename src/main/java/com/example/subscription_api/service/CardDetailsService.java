package com.example.subscription_api.service;

import com.example.subscription_api.dto.cards_details.CardsDetailsRequestDTO;
import com.example.subscription_api.dto.cards_details.CardsDetailsResponseDTO;
import com.example.subscription_api.entity.CardsDetails;
import com.example.subscription_api.entity.User;
import com.example.subscription_api.exception.ResourceNotFoundException;
import com.example.subscription_api.mapper.CardsDetailsMapper;
import com.example.subscription_api.repository.CardsDetailsRepository;
import com.example.subscription_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardDetailsService {

    private final CardsDetailsRepository cardsDetailsRepository;
    private final UserRepository userRepository;
    private final CardsDetailsMapper cardsDetailsMapper;

    public List<CardsDetailsResponseDTO> getAllCardsByUserId(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return cardsDetailsRepository.findByUserId(userId)
                .stream()
                .map(cardsDetailsMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CardsDetailsResponseDTO createCard(String userId, CardsDetailsRequestDTO requestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<CardsDetails> existingCards = cardsDetailsRepository.findByUserId(userId);

        boolean isFirstCard = existingCards.isEmpty();
        boolean isRequestedDefault = requestDTO.getIsDefault() != null && requestDTO.getIsDefault();
        boolean shouldBeDefault = isFirstCard || isRequestedDefault;

        if (shouldBeDefault && !isFirstCard) {
            existingCards.forEach(card -> card.setDefault(false));
            cardsDetailsRepository.saveAll(existingCards);
        }

        CardsDetails cardDetails = CardsDetails.builder()
                .user(user)
                .cardHolderName(requestDTO.getCardHolderName())
                .cardBrand(requestDTO.getCardBrand())
                .lastFourDigits(requestDTO.getLastFourDigits())
                .isDefault(shouldBeDefault)
                .build();

        CardsDetails savedCard = cardsDetailsRepository.save(cardDetails);
        return cardsDetailsMapper.toResponseDTO(savedCard);
    }

    public CardsDetailsResponseDTO updateCard(String userId, String cardId, CardsDetailsRequestDTO requestDTO) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        CardsDetails existingCard = cardsDetailsRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));

        if (!existingCard.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Card does not belong to the specified user");
        }

        existingCard.setCardHolderName(requestDTO.getCardHolderName());
        existingCard.setCardBrand(requestDTO.getCardBrand());
        existingCard.setLastFourDigits(requestDTO.getLastFourDigits());

        CardsDetails updatedCard = cardsDetailsRepository.save(existingCard);
        return cardsDetailsMapper.toResponseDTO(updatedCard);
    }

    public void deleteCard(String userId, String cardId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        CardsDetails existingCard = cardsDetailsRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));

        if (!existingCard.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Card does not belong to the specified user");
        }

        cardsDetailsRepository.delete(existingCard);
    }

    @Transactional
    public void setDefaultCard(String userId, String cardId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        List<CardsDetails> userCards = cardsDetailsRepository.findByUserId(userId);

        CardsDetails targetCard = userCards.stream()
                .filter(card -> card.getId().equals(cardId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Card not found for this user with id: " + cardId));

        userCards.forEach(card -> card.setDefault(false));
        targetCard.setDefault(true);

        cardsDetailsRepository.saveAll(userCards);
    }


}