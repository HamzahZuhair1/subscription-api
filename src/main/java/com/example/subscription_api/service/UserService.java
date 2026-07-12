package com.example.subscription_api.service;

import com.example.subscription_api.dto.user.UserRequestDTO;
import com.example.subscription_api.dto.user.UserResponseDTO;
import com.example.subscription_api.entity.User;
import com.example.subscription_api.mapper.UserMapper;
import com.example.subscription_api.repository.UserRepository;
import com.example.subscription_api.exception.ResourceNotFoundException;
import com.example.subscription_api.exception.DuplicateResourceException;
import com.example.subscription_api.validation.PhoneNumberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PhoneNumberValidator phoneNumberValidator;
    private final UserMapper userMapper;

    public UserResponseDTO createUser(UserRequestDTO requestDTO) {

        phoneNumberValidator.validate(requestDTO.getMobileNumber());

        Optional<User> existingUser = userRepository.findFirstByEmailOrMobileNumber(requestDTO.getEmail(), requestDTO.getMobileNumber());
        if (existingUser.isPresent()) {
            if (existingUser.get().getEmail().equals(requestDTO.getEmail()))
                throw new DuplicateResourceException("Email is already in use");

            if (existingUser.get().getMobileNumber().equals(requestDTO.getMobileNumber()))
                throw new DuplicateResourceException("Mobile number is already in use");

        }

        User user = User.builder()
                .firstName(requestDTO.getFirstName())
                .lastName(requestDTO.getLastName())
                .email(requestDTO.getEmail())
                .mobileNumber(requestDTO.getMobileNumber())
                .build();

        User savedUser = userRepository.save(user);
        return userMapper.toResponseDTO(savedUser);
    }

    public UserResponseDTO getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toResponseDTO(user);
    }

    public Page<UserResponseDTO> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable)
                .map(userMapper::toResponseDTO);
    }

    public UserResponseDTO updateUser(String id, UserRequestDTO requestDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        phoneNumberValidator.validate(requestDTO.getMobileNumber());

        boolean emailChanged = !existingUser.getEmail().equals(requestDTO.getEmail());
        boolean phoneChanged = !existingUser.getMobileNumber().equals(requestDTO.getMobileNumber());

        if (emailChanged || phoneChanged) {
            Optional<User> conflictUser = userRepository.findFirstByEmailOrMobileNumber(requestDTO.getEmail(), requestDTO.getMobileNumber());

            if (conflictUser.isPresent() && !conflictUser.get().getId().equals(id)) {
                if (conflictUser.get().getEmail().equals(requestDTO.getEmail()))
                    throw new DuplicateResourceException("Email is already in use by another user");

                if (conflictUser.get().getMobileNumber().equals(requestDTO.getMobileNumber()))
                    throw new DuplicateResourceException("Mobile number is already in use by another user");

            }
        }

        existingUser.setFirstName(requestDTO.getFirstName());
        existingUser.setLastName(requestDTO.getLastName());
        existingUser.setEmail(requestDTO.getEmail());
        existingUser.setMobileNumber(requestDTO.getMobileNumber());

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toResponseDTO(updatedUser);
    }

    public void deleteUser(String id) {
        int numberOfDeletedUsers = userRepository.deleteByIdEquals(id);
        if (numberOfDeletedUsers == 0){
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
    }
}