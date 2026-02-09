/*
 * package com.standard.service.service.impl;
 * 
 * import com.standard.service.dto.UserDTO; import
 * com.standard.service.entity.User; import
 * com.standard.service.exception.ErrorCode; import
 * com.standard.service.exception.ResourceNotFoundException; import
 * com.standard.service.exception.ServiceException; import
 * com.standard.service.repository.UserRepository; import
 * com.standard.service.service.ResilientUserService; import
 * io.github.resilience4j.bulkhead.annotation.Bulkhead; import
 * io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker; import
 * io.github.resilience4j.ratelimiter.annotation.RateLimiter; import
 * io.github.resilience4j.retry.annotation.Retry; import
 * io.micrometer.observation.annotation.Observed; import
 * lombok.RequiredArgsConstructor; import lombok.extern.slf4j.Slf4j; import
 * org.springframework.cache.annotation.Cacheable; import
 * org.springframework.stereotype.Service; import
 * org.springframework.transaction.annotation.Transactional;
 * 
 * /** Implementation of ResilientUserService with database-level resilience
 * patterns.
 */
/*
 * @Slf4j
 * 
 * @Service
 * 
 * @RequiredArgsConstructor
 * 
 * @Observed(name = "resilient.user.service") public class
 * ResilientUserServiceImpl implements ResilientUserService {
 * 
 * private final UserRepository userRepository; private static final String
 * USER_SERVICE = "userService";
 * 
 * @Override
 * 
 * @Transactional(readOnly = true)
 * 
 * @CircuitBreaker(name = USER_SERVICE, fallbackMethod = "getUserFallback")
 * 
 * @Retry(name = USER_SERVICE)
 * 
 * @Cacheable(value = "users", key = "#id") public UserDTO
 * getUserWithResilience(Long id) {
 * log.info("Fetching user with resilience patterns: {}", id);
 * 
 * User user = userRepository.findById(id) .orElseThrow(() -> new
 * ResourceNotFoundException("User", "id", id));
 * 
 * return mapToDTO(user); }
 * 
 * private UserDTO getUserFallback(Long id, Exception ex) {
 * log.warn("Circuit breaker fallback triggered for user {}: {}", id,
 * ex.getMessage());
 * 
 * return UserDTO.builder() .id(id) .username("fallback-user")
 * .email("fallback@example.com") .firstName("Fallback") .lastName("User")
 * .status(User.UserStatus.ACTIVE) .build(); }
 * 
 * @Override
 * 
 * @Transactional
 * 
 * @RateLimiter(name = USER_SERVICE, fallbackMethod =
 * "createUserRateLimitFallback")
 * 
 * @CircuitBreaker(name = USER_SERVICE) public UserDTO
 * createUserWithRateLimit(UserDTO userDTO) {
 * log.info("Creating user with rate limiting: {}", userDTO.getUsername());
 * 
 * User user = mapToEntity(userDTO); User savedUser = userRepository.save(user);
 * 
 * return mapToDTO(savedUser); }
 * 
 * private UserDTO createUserRateLimitFallback(UserDTO userDTO, Exception ex) {
 * log.warn("Rate limit exceeded for user creation: {}", userDTO.getUsername());
 * throw new ServiceException(ErrorCode.RATE_LIMIT_EXCEEDED); }
 * 
 * @Override
 * 
 * @Transactional(readOnly = true)
 * 
 * @Bulkhead(name = USER_SERVICE, fallbackMethod = "countUsersBulkheadFallback")
 * 
 * @Retry(name = USER_SERVICE) public long countActiveUsers() {
 * log.info("Counting active users with bulkhead protection"); return
 * userRepository.count(); }
 * 
 * private long countUsersBulkheadFallback(Exception ex) {
 * log.warn("Bulkhead limit reached for counting users"); return -1L; }
 * 
 * @Override
 * 
 * @Transactional
 * 
 * @CircuitBreaker(name = USER_SERVICE, fallbackMethod = "updateUserFallback")
 * 
 * @Retry(name = USER_SERVICE)
 * 
 * @RateLimiter(name = USER_SERVICE)
 * 
 * @Bulkhead(name = USER_SERVICE) public UserDTO
 * updateUserWithFullResilience(Long id, UserDTO userDTO) {
 * log.info("Updating user with full resilience patterns: {}", id);
 * 
 * User existingUser = userRepository.findById(id) .orElseThrow(() -> new
 * ResourceNotFoundException("User", "id", id));
 * 
 * existingUser.setUsername(userDTO.getUsername());
 * existingUser.setEmail(userDTO.getEmail());
 * existingUser.setFirstName(userDTO.getFirstName());
 * existingUser.setLastName(userDTO.getLastName());
 * 
 * User updatedUser = userRepository.save(existingUser); return
 * mapToDTO(updatedUser); }
 * 
 * private UserDTO updateUserFallback(Long id, UserDTO userDTO, Exception ex) {
 * log.error("All resilience patterns triggered for user update {}: {}", id,
 * ex.getMessage()); throw new ServiceException(ErrorCode.SERVICE_UNAVAILABLE,
 * "User update service busy"); }
 * 
 * private UserDTO mapToDTO(User user) { return UserDTO.builder()
 * .id(user.getId()) .username(user.getUsername()) .email(user.getEmail())
 * .firstName(user.getFirstName()) .lastName(user.getLastName())
 * .phoneNumber(user.getPhoneNumber()) .status(user.getStatus())
 * .createdAt(user.getCreatedAt()) .updatedAt(user.getUpdatedAt()) .build(); }
 * 
 * private User mapToEntity(UserDTO dto) { return User.builder()
 * .username(dto.getUsername()) .email(dto.getEmail())
 * .firstName(dto.getFirstName()) .lastName(dto.getLastName())
 * .phoneNumber(dto.getPhoneNumber()) .status(dto.getStatus() != null ?
 * dto.getStatus() : User.UserStatus.ACTIVE) .build(); } }
 */