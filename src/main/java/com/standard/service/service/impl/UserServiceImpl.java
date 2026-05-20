package com.standard.service.service.impl;

import com.standard.service.dto.UserDto;
import com.standard.service.entity.UserEntity;
import com.standard.service.repository.UserRepository;
import com.standard.service.service.UserService;
import com.standard.service.utils.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EncryptionService encryptionService;

    @Override
    public UserDto createUser(UserDto userDto) {
        UserEntity entity = new UserEntity();
        entity.setName(userDto.getName());
        entity.setEmail(userDto.getEmail());
        entity.setNationalId(userDto.getNationalId());

        UserEntity saved = userRepository.save(entity);

        UserDto result = mapToDto(saved);
        // Ensure the returned DTO has the plaintext values (transparent to the client),
        // as the saved entity in Hibernate L1 cache may retain the encrypted state from onSave.
        result.setEmail(userDto.getEmail());
        result.setNationalId(userDto.getNationalId());
        return result;
    }

    @Override
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id).map(this::mapToDto);
    }

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        String emailHash = encryptionService.generateBlindIndex(email);
        return userRepository.findByEmailHash(emailHash).map(this::mapToDto);
    }

    @Override
    public Optional<UserDto> getUserByNationalId(String nationalId) {
        if (nationalId == null || nationalId.isBlank()) {
            return Optional.empty();
        }
        String nationalIdHash = encryptionService.generateBlindIndex(nationalId);
        return userRepository.findByNationalIdHash(nationalIdHash).map(this::mapToDto);
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
