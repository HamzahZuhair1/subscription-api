package com.example.subscription_api.service;

import com.example.subscription_api.dto.country.CountryRequestDTO;
import com.example.subscription_api.dto.country.CountryResponseDTO;
import com.example.subscription_api.entity.Country;
import com.example.subscription_api.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryResponseDTO createCountry(CountryRequestDTO requestDTO) {
        Country country = Country.builder()
                .name(requestDTO.getName())
                .code(requestDTO.getCode())
                .build();

        Country savedCountry = countryRepository.save(country);
        return mapToResponseDTO(savedCountry);
    }

    public List<CountryResponseDTO> getAllCountries() {
        return countryRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public CountryResponseDTO getCountryById(String id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found with id: " + id));
        return mapToResponseDTO(country);
    }

    public CountryResponseDTO updateCountry(String id, CountryRequestDTO requestDTO) {
        Country existingCountry = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found with id: " + id));

        existingCountry.setName(requestDTO.getName());
        existingCountry.setCode(requestDTO.getCode());

        Country updatedCountry = countryRepository.save(existingCountry);
        return mapToResponseDTO(updatedCountry);
    }

    public void deleteCountry(String id) {
        Country existingCountry = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found with id: " + id));
        countryRepository.delete(existingCountry);
    }

    private CountryResponseDTO mapToResponseDTO(Country country) {
        return CountryResponseDTO.builder()
                .id(country.getId())
                .name(country.getName())
                .code(country.getCode())
                .build();
    }
}