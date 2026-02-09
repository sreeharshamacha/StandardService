package com.standard.service.service.impl;

import com.standard.service.dto.PaginatedResponse;
import com.standard.service.dto.PaginationRequest;
import com.standard.service.dto.ResourceRequest;
import com.standard.service.dto.ResourceResponse;
import com.standard.service.entity.Resource;
import com.standard.service.exception.ResourceNotFoundException;
import com.standard.service.repository.ResourceRepository;
import com.standard.service.service.ResourceService;
import com.standard.service.utils.GenericSpecification;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;

    @Override
    @Transactional
    @CacheEvict(value = "resources", allEntries = true)
    public ResourceResponse createResource(ResourceRequest request) {
        log.info("Creating new resource with name: {}", request.getName());
        Resource resource = Resource.builder().name(request.getName()).description(request.getDescription()).build();

        Resource savedResource = resourceRepository.save(resource);
        return mapToResponse(savedResource);
    }

    @Override
    @Cacheable(value = "resources", key = "#id")
    @CircuitBreaker(name = "mainService")
    public ResourceResponse getResource(Long id) {
        log.info("Fetching resource with id: {}", id);
        Resource resource = resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        return mapToResponse(resource);
    }

    @Override
    public List<ResourceResponse> getAllResources() {
        log.info("Fetching all resources");
        return resourceRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public PaginatedResponse<ResourceResponse> searchResources(PaginationRequest request) {
        log.info("Searching resources with pagination: {}", request);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()),
                request.getSortBy() != null ? request.getSortBy() : "id");

        Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), sort);

        GenericSpecification<Resource> specification = new GenericSpecification<>(request.getFilters());

        Page<Resource> page = resourceRepository.findAll(specification, pageable);

        List<ResourceResponse> content = page.getContent().stream().map(this::mapToResponse)
                .collect(Collectors.toList());

        return PaginatedResponse.<ResourceResponse>builder().content(content).pageNumber(page.getNumber())
                .pageSize(page.getSize()).totalElements(page.getTotalElements()).totalPages(page.getTotalPages())
                .last(page.isLast()).build();
    }

    private ResourceResponse mapToResponse(Resource resource) {
        return ResourceResponse.builder().id(resource.getId()).name(resource.getName())
                .description(resource.getDescription()).createdAt(resource.getCreatedAt())
                .updatedAt(resource.getUpdatedAt()).createdBy(resource.getCreatedBy())
                .updatedBy(resource.getUpdatedBy()).build();
    }
}
