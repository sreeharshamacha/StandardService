package com.standard.service.service.impl;

import com.standard.service.dto.ResourceRequest;
import com.standard.service.dto.ResourceResponse;
import com.standard.service.entity.Resource;
import com.standard.service.exception.ResourceNotFoundException;
import com.standard.service.repository.ResourceRepository;
import com.standard.service.service.ResourceService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;

    @Override
    @Transactional
    @CacheEvict(value = "resources", allEntries = true)
    public ResourceResponse createResource(ResourceRequest request) {
        Resource resource = Resource.builder().name(request.getName()).description(request.getDescription()).build();

        Resource savedResource = resourceRepository.save(resource);
        return mapToResponse(savedResource);
    }

    @Override
    @Cacheable(value = "resources", key = "#id")
    @CircuitBreaker(name = "mainService")
    public ResourceResponse getResource(Long id) {
        Resource resource = resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        return mapToResponse(resource);
    }

    @Override
    public List<ResourceResponse> getAllResources() {
        return resourceRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private ResourceResponse mapToResponse(Resource resource) {
        return ResourceResponse.builder().id(resource.getId()).name(resource.getName())
                .description(resource.getDescription()).createdAt(resource.getCreatedAt())
                .updatedAt(resource.getUpdatedAt()).createdBy(resource.getCreatedBy())
                .updatedBy(resource.getUpdatedBy()).build();
    }
}
