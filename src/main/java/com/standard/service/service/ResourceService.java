package com.standard.service.service;

import com.standard.service.dto.PaginatedResponse;
import com.standard.service.dto.PaginationRequest;
import com.standard.service.dto.ResourceRequest;
import com.standard.service.dto.ResourceResponse;

import java.util.List;

public interface ResourceService {
    ResourceResponse createResource(ResourceRequest request);

    ResourceResponse getResource(Long id);

    List<ResourceResponse> getAllResources();

    PaginatedResponse<ResourceResponse> searchResources(PaginationRequest request);
}
