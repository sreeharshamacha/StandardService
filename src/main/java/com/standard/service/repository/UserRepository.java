package com.standard.service.repository;

import com.standard.service.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailHash(String emailHash);
    Optional<UserEntity> findByNationalIdHash(String nationalIdHash);
}
