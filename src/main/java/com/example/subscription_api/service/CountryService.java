package com.example.subscription_api.service;

import com.example.subscription_api.dto.country.CountryRequestDTO;
import com.example.subscription_api.dto.country.CountryResponseDTO;
import com.example.subscription_api.entity.Country;
import com.example.subscription_api.exception.ResourceNotFoundException;
import com.example.subscription_api.mapper.CountryMapper;
import com.example.subscription_api.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;
    public CountryResponseDTO createCountry(CountryRequestDTO requestDTO) {
        Country country = Country.builder()
                .name(requestDTO.getName())
                .code(requestDTO.getCode())
                .build();

        Country savedCountry = countryRepository.save(country);
        return countryMapper.toResponseDTO(savedCountry);
    }

    public List<CountryResponseDTO> getAllCountries() {
        return countryRepository.findAll()
                .stream()
                .map(countryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CountryResponseDTO getCountryById(String id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + id));
        return countryMapper.toResponseDTO(country);
    }

    public CountryResponseDTO updateCountry(String id, CountryRequestDTO requestDTO) {
        Country existingCountry = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + id));

        existingCountry.setName(requestDTO.getName());
        existingCountry.setCode(requestDTO.getCode());

        Country updatedCountry = countryRepository.save(existingCountry);
        return countryMapper.toResponseDTO(updatedCountry);
    }

    public void deleteCountry(String id) {
        if (countryRepository.deleteByIdEquals(id) == 0) {
            throw new ResourceNotFoundException("Country not found with id: " + id);
        }
    }


}