package com.example.subscription_api.controller;

import com.example.subscription_api.dto.country.CountryRequestDTO;
import com.example.subscription_api.dto.country.CountryResponseDTO;
import com.example.subscription_api.service.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @PostMapping
    public ResponseEntity<CountryResponseDTO> createCountry(@Valid @RequestBody CountryRequestDTO requestDTO) {
        CountryResponseDTO createdCountry = countryService.createCountry(requestDTO);
        return new ResponseEntity<>(createdCountry, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CountryResponseDTO>> getAllCountries() {
        return ResponseEntity.ok(countryService.getAllCountries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryResponseDTO> getCountryById(@PathVariable String id) {
        return ResponseEntity.ok(countryService.getCountryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CountryResponseDTO> updateCountry(@PathVariable String id, @Valid @RequestBody CountryRequestDTO requestDTO) {
        return ResponseEntity.ok(countryService.updateCountry(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable String id) {
        countryService.deleteCountry(id);
        return ResponseEntity.noContent().build();
    }
}