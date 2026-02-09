package com.standard.service.utils;

import com.standard.service.dto.FilterRequest;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class GenericSpecification<T> implements Specification<T> {

    private final List<FilterRequest> filters;

    public GenericSpecification(List<FilterRequest> filters) {
        this.filters = filters != null ? filters : new ArrayList<>();
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        for (FilterRequest filter : filters) {
            String column = filter.getColumn();
            String value = filter.getValue();
            String operator = filter.getOperator() != null ? filter.getOperator().toUpperCase() : "EQUALS";

            switch (operator) {
            case "EQUALS" -> predicates.add(cb.equal(root.get(column), value));
            case "LIKE" -> predicates.add(cb.like(cb.lower(root.get(column)), "%" + value.toLowerCase() + "%"));
            case "GREATER_THAN" -> predicates.add(cb.greaterThan(root.get(column), value));
            case "LESS_THAN" -> predicates.add(cb.lessThan(root.get(column), value));
            case "IN" -> {
                CriteriaBuilder.In<Object> inClause = cb.in(root.get(column));
                for (String v : value.split(",")) {
                    inClause.value(v.trim());
                }
                predicates.add(inClause);
            }
            default -> predicates.add(cb.equal(root.get(column), value));
            }
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
