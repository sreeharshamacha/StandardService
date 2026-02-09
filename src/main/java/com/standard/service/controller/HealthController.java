package com.standard.service.controller;

import com.standard.service.constant.AppConstants;
import com.standard.service.utils.MessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(AppConstants.API_V1_PREFIX + "/health")
@Tag(name = "Health Check", description = "Monitor service status")
@RequiredArgsConstructor
public class HealthController {

    private final MessageUtils messageUtils;

    @GetMapping
    @Operation(summary = "Check service health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        String message = messageUtils.getMessage("resource.health.up");
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "message", message));
    }
}
