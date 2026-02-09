package com.standard.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {
    @Builder.Default
    private int pageNumber = 0;

    @Builder.Default
    private int pageSize = 10;

    private String sortBy;

    @Builder.Default
    private String sortDirection = "DESC"; // ASC or DESC

    @Builder.Default
    private List<FilterRequest> filters = new ArrayList<>();
}
