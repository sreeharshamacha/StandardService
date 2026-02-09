/*
 * package com.standard.service.controller;
 * 
 * import com.standard.service.dto.ApiResponse; import
 * com.standard.service.service.ExternalApiService; import
 * com.standard.service.utils.MdcUtils; import
 * io.swagger.v3.oas.annotations.Operation; import
 * io.swagger.v3.oas.annotations.Parameter; import
 * io.swagger.v3.oas.annotations.tags.Tag; import
 * lombok.RequiredArgsConstructor; import lombok.extern.slf4j.Slf4j; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.security.access.prepost.PreAuthorize; import
 * org.springframework.web.bind.annotation.*;
 * 
 * import java.util.concurrent.CompletableFuture;
 * 
 * @Slf4j
 * 
 * @RestController
 * 
 * @RequestMapping("/api/resilient")
 * 
 * @RequiredArgsConstructor
 * 
 * @Tag(name = "Resilient API", description =
 * "APIs demonstrating Resilience4j patterns") public class ResilientController
 * {
 * 
 * private final ExternalApiService externalApiService;
 * 
 * @GetMapping("/users/{userId}")
 * 
 * @Operation(summary = "Get external user with Circuit Breaker", description =
 * "Demonstrates Circuit Breaker pattern with fallback")
 * 
 * @PreAuthorize("hasAnyRole('USER', 'ADMIN')") public
 * ResponseEntity<ApiResponse<String>> getExternalUser(
 * 
 * @Parameter(description = "User ID") @PathVariable Long userId) {
 * log.info("Request received for external user: {}", userId); String result =
 * externalApiService.getExternalUserData(userId); return
 * ResponseEntity.ok(ApiResponse.success(result, MdcUtils.getTraceId())); }
 * 
 * @GetMapping("/retry/{endpoint}")
 * 
 * @Operation(summary = "Fetch data with retry", description =
 * "Demonstrates Retry pattern with exponential backoff")
 * 
 * @PreAuthorize("hasAnyRole('USER', 'ADMIN')") public
 * ResponseEntity<ApiResponse<String>> fetchWithRetry(
 * 
 * @Parameter(description = "API endpoint") @PathVariable String endpoint) {
 * log.info("Request with retry for endpoint: {}", endpoint); String result =
 * externalApiService.fetchDataWithRetry(endpoint); return
 * ResponseEntity.ok(ApiResponse.success(result, MdcUtils.getTraceId())); }
 * 
 * @PostMapping("/rate-limited")
 * 
 * @Operation(summary = "Rate limited API call", description =
 * "Demonstrates Rate Limiter pattern")
 * 
 * @PreAuthorize("hasAnyRole('USER', 'ADMIN')") public
 * ResponseEntity<ApiResponse<String>> rateLimitedCall(
 * 
 * @Parameter(description = "Request payload") @RequestBody String data) {
 * log.info("Rate limited request received"); String result =
 * externalApiService.callRateLimitedApi(data); return
 * ResponseEntity.ok(ApiResponse.success(result, MdcUtils.getTraceId())); }
 * 
 * @GetMapping("/bulkhead/{id}")
 * 
 * @Operation(summary = "Bulkhead protected call", description =
 * "Demonstrates Bulkhead pattern to limit concurrent calls")
 * 
 * @PreAuthorize("hasAnyRole('USER', 'ADMIN')") public
 * ResponseEntity<ApiResponse<String>> bulkheadCall(
 * 
 * @Parameter(description = "Resource ID") @PathVariable Long id) {
 * log.info("Bulkhead protected request for id: {}", id); String result =
 * externalApiService.callWithBulkhead(id); return
 * ResponseEntity.ok(ApiResponse.success(result, MdcUtils.getTraceId())); }
 * 
 * @GetMapping("/timeout/{id}")
 * 
 * @Operation(summary = "Time limited async call", description =
 * "Demonstrates Time Limiter pattern for async operations")
 * 
 * @PreAuthorize("hasAnyRole('USER', 'ADMIN')") public
 * CompletableFuture<ResponseEntity<ApiResponse<String>>> timeoutCall(
 * 
 * @Parameter(description = "Resource ID") @PathVariable Long id) {
 * log.info("Async time limited request for id: {}", id); return
 * externalApiService.callWithTimeout(id) .thenApply(result ->
 * ResponseEntity.ok(ApiResponse.success(result, MdcUtils.getTraceId()))); }
 * 
 * @PostMapping("/robust/{endpoint}")
 * 
 * @Operation(summary = "Robust API call with all patterns", description =
 * "Demonstrates combination of Circuit Breaker, Retry, Rate Limiter, and Bulkhead"
 * )
 * 
 * @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponse<String>>
 * robustCall(
 * 
 * @Parameter(description = "API endpoint") @PathVariable String endpoint,
 * 
 * @Parameter(description = "Request payload") @RequestBody String payload) {
 * log.info("Robust request for endpoint: {}", endpoint); String result =
 * externalApiService.robustApiCall(endpoint, payload); return
 * ResponseEntity.ok(ApiResponse.success(result, MdcUtils.getTraceId())); } }
 */