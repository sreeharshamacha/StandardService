package com.standard.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest {
    private String column;
    private String value;
    private String operator; // EQUALS, LIKE, GREATER_THAN, LESS_THAN, IN
}
