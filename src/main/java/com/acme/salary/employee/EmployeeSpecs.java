package com.acme.salary.employee;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class EmployeeSpecs {
    private EmployeeSpecs() {}

    public static Specification<Employee> from(EmployeeFilter f) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(f.search())) {
                String like = "%" + f.search().trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("fullName")), like),
                        cb.like(cb.lower(root.get("email")), like)));
            }
            if (StringUtils.hasText(f.country())) predicates.add(cb.equal(root.get("country"), f.country()));
            if (StringUtils.hasText(f.department())) predicates.add(cb.equal(root.get("department"), f.department()));
            if (StringUtils.hasText(f.jobTitle())) predicates.add(cb.equal(root.get("jobTitle"), f.jobTitle()));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
