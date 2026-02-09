/*
 * package com.standard.service.service.impl;
 * 
 * import com.standard.service.service.ExternalApiService; import
 * com.standard.service.exception.ErrorCode; import
 * com.standard.service.exception.ServiceException; import
 * io.github.resilience4j.bulkhead.annotation.Bulkhead; import
 * io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker; import
 * io.github.resilience4j.ratelimiter.annotation.RateLimiter; import
 * io.github.resilience4j.retry.annotation.Retry; import
 * io.github.resilience4j.timelimiter.annotation.TimeLimiter; import
 * lombok.extern.slf4j.Slf4j; import org.springframework.stereotype.Service;
 * 
 * import java.util.concurrent.CompletableFuture; import
 * java.util.concurrent.TimeUnit;
 * 
 * @Slf4j
 * 
 * @Service public class ExternalApiServiceImpl implements ExternalApiService {
 * 
 * private static final String MAIN_SERVICE = "mainService";
 * 
 * @Override
 * 
 * @CircuitBreaker(name = MAIN_SERVICE, fallbackMethod = "fallbackForUserData")
 * public String getExternalUserData(Long userId) {
 * log.info("Calling external user data API for user: {}", userId); // Simulate
 * external call if (userId == 0) { throw new
 * ServiceException(ErrorCode.EXTERNAL_SERVICE_ERROR, "User ID 0 is invalid"); }
 * return "External data for user " + userId; }
 * 
 * public String fallbackForUserData(Long userId, Throwable t) {
 * log.warn("Fallback triggered for user {}: {}", userId, t.getMessage());
 * return "Fallback user data (Cached/Static)"; }
 * 
 * @Override
 * 
 * @Retry(name = MAIN_SERVICE, fallbackMethod = "fallbackForRetry") public
 * String fetchDataWithRetry(String endpoint) {
 * log.info("Fetching data from {} with retry", endpoint); if
 * ("fail".equalsIgnoreCase(endpoint)) { throw new
 * ServiceException(ErrorCode.EXTERNAL_SERVICE_ERROR,
 * "Temporary API failure at " + endpoint); } return "Data from " + endpoint; }
 * 
 * public String fallbackForRetry(String endpoint, Throwable t) { return
 * "Retry failed. Returning fallback for " + endpoint; }
 * 
 * @Override
 * 
 * @RateLimiter(name = MAIN_SERVICE) public String callRateLimitedApi(String
 * data) { return "Rate limited response for: " + data; }
 * 
 * @Override
 * 
 * @Bulkhead(name = MAIN_SERVICE) public String callWithBulkhead(Long id) {
 * return "Bulkhead protected data for " + id; }
 * 
 * @Override
 * 
 * @TimeLimiter(name = MAIN_SERVICE) public CompletableFuture<String>
 * callWithTimeout(Long id) { return CompletableFuture.supplyAsync(() -> { try {
 * if (id == 999) { TimeUnit.SECONDS.sleep(5); // Longer than timeout } return
 * "Async data for " + id; } catch (InterruptedException e) {
 * Thread.currentThread().interrupt(); throw new
 * ServiceException(ErrorCode.ASYNC_EXECUTION_ERROR, e.getMessage()); } }); }
 * 
 * @Override
 * 
 * @CircuitBreaker(name = MAIN_SERVICE)
 * 
 * @Retry(name = MAIN_SERVICE)
 * 
 * @RateLimiter(name = MAIN_SERVICE)
 * 
 * @Bulkhead(name = MAIN_SERVICE) public String robustApiCall(String endpoint,
 * String payload) { return "Robust response from " + endpoint +
 * " with payload: " + payload; } }
 */