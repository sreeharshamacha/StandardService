package com.standard.service.service;

import com.standard.service.dto.UserDto;
import java.util.Optional;

public interface UserService {
    UserDto createUser(UserDto userDto);
    Optional<UserDto> getUserById(Long id);
    Optional<UserDto> getUserByEmail(String email);
    Optional<UserDto> getUserByNationalId(String nationalId);
}
