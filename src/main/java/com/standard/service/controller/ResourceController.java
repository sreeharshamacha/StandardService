package com.standard.service.controller;

import com.standard.service.constant.AppConstants;
import com.standard.service.dto.ApiResponse;
import com.standard.service.dto.ResourceRequest;
import com.standard.service.dto.ResourceResponse;
import com.standard.service.service.ResourceService;
import com.standard.service.utils.MdcUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppConstants.API_V1_PREFIX + "/resources")
@RequiredArgsConstructor
@Tag(name = "Resource Management", description = "Endpoints for managing resources")
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping
    @Operation(summary = "Create a new resource")
    public ResponseEntity<ApiResponse<ResourceResponse>> createResource(@RequestBody ResourceRequest request) {
        ResourceResponse response = resourceService.createResource(request);
        return new ResponseEntity<>(ApiResponse.success(response, MdcUtils.getTraceId()), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a resource by ID")
    public ResponseEntity<ApiResponse<ResourceResponse>> getResource(@PathVariable Long id) {
        ResourceResponse response = resourceService.getResource(id);
        return ResponseEntity.ok(ApiResponse.success(response, MdcUtils.getTraceId()));
    }

    @GetMapping
    @Operation(summary = "Get all resources")
    public ResponseEntity<ApiResponse<List<ResourceResponse>>> getAllResources() {
        List<ResourceResponse> response = resourceService.getAllResources();
        return ResponseEntity.ok(ApiResponse.success(response, MdcUtils.getTraceId()));
    }
}
