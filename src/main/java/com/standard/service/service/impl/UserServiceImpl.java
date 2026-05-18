package com.standard.service.service.impl;

import com.standard.service.dto.UserDto;
import com.standard.service.entity.UserEntity;
import com.standard.service.repository.UserRepository;
import com.standard.service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        UserEntity entity = new UserEntity();
        entity.setName(userDto.getName());
        entity.setEmail(userDto.getEmail());
        entity.setNationalId(userDto.getNationalId());

        UserEntity saved = userRepository.save(entity);

        return mapToDto(saved);
    }

    @Override
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id).map(this::mapToDto);
    }

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        // Not implemented in repository by default. Requires searching by emailHash.
        return Optional.empty(); 
    }

    private UserDto mapToDto(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .nationalId(entity.getNationalId())
                .build();
    }
}
