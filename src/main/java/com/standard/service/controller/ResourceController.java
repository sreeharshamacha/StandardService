package com.standard.service.controller;

import com.standard.service.constant.AppConstants;
import com.standard.service.dto.ApiResponse;
import com.standard.service.dto.PaginatedResponse;
import com.standard.service.dto.PaginationRequest;
import com.standard.service.dto.ResourceRequest;
import com.standard.service.dto.ResourceResponse;
import com.standard.service.service.ResourceService;
import com.standard.service.utils.MdcUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(AppConstants.API_V1_PREFIX + "/resources")
@RequiredArgsConstructor
@Tag(name = "Resource Management", description = "Endpoints for managing resources")
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping
    @Operation(summary = "Create a new resource")
    public ResponseEntity<ApiResponse<ResourceResponse>> createResource(@RequestBody ResourceRequest request) {
        log.info("REST request to create resource: {}", request.getName());
        ResourceResponse response = resourceService.createResource(request);
        return new ResponseEntity<>(ApiResponse.success(response, MdcUtils.getTraceId()), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a resource by ID")
    public ResponseEntity<ApiResponse<ResourceResponse>> getResource(@PathVariable Long id) {
        log.info("REST request to get resource: {}", id);
        ResourceResponse response = resourceService.getResource(id);
        return ResponseEntity.ok(ApiResponse.success(response, MdcUtils.getTraceId()));
    }

    @GetMapping
    @Operation(summary = "Get all resources with pagination")
    public ResponseEntity<ApiResponse<PaginatedResponse<ResourceResponse>>> getAllResources(PaginationRequest request) {
        log.info("REST request to get all resources with pagination: {}", request);
        PaginatedResponse<ResourceResponse> response = resourceService.searchResources(request);
        return ResponseEntity.ok(ApiResponse.success(response, MdcUtils.getTraceId()));
    }

    @PostMapping("/search")
    @Operation(summary = "Search resources with pagination and filtering")
    public ResponseEntity<ApiResponse<PaginatedResponse<ResourceResponse>>> searchResources(
            @RequestBody PaginationRequest request) {
        log.info("REST request to search resources: {}", request);
        PaginatedResponse<ResourceResponse> response = resourceService.searchResources(request);
        return ResponseEntity.ok(ApiResponse.success(response, MdcUtils.getTraceId()));
    }
}
