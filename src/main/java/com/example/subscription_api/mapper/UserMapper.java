package com.example.subscription_api.mapper;

import com.example.subscription_api.dto.user.UserRequestDTO;
import com.example.subscription_api.dto.user.UserResponseDTO;
import com.example.subscription_api.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserResponseDTO toResponseDTO(User user);

    User toEntity(UserRequestDTO requestDTO);
}