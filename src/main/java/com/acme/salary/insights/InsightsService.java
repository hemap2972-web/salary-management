package com.acme.salary.insights;

import com.acme.salary.common.Stats;
import com.acme.salary.employee.EmployeeRepository;
import com.acme.salary.employee.EmployeeResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InsightsService {
    private final InsightsRepository insights;
    private final EmployeeRepository employees;

    public InsightsService(InsightsRepository insights, EmployeeRepository employees) {
        this.insights = insights;
        this.employees = employees;
    }

    @Transactional(readOnly = true)
    public SalarySummary summary() {
        List<Long> sorted = insights.allSalariesSorted();
        if (sorted.isEmpty()) return new SalarySummary(0, 0, 0, 0, 0, 0);
        long total = sorted.stream().mapToLong(Long::longValue).sum();
        return new SalarySummary(sorted.size(), total, sorted.get(0), sorted.get(sorted.size() - 1),
                Math.round((double) total / sorted.size()), Stats.median(sorted));
    }

    @Transactional(readOnly = true)
    public List<GroupStat> by(Dimension dimension) {
        Map<String, List<Long>> salaries = new LinkedHashMap<>();
        for (Object[] row : insights.orderedSalaries(dimension)) {
            salaries.computeIfAbsent((String) row[0], k -> new ArrayList<>()).add(((Number) row[1]).longValue());
        }
        List<GroupStat> result = new ArrayList<>();
        for (Object[] row : insights.aggregates(dimension)) {
            String name = (String) row[0];
            result.add(new GroupStat(name,
                    ((Number) row[1]).longValue(),
                    ((Number) row[2]).longValue(),
                    ((Number) row[3]).longValue(),
                    Math.round(((Number) row[4]).doubleValue()),
                    Stats.median(salaries.get(name))));
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> earners(String order, int limit) {
        Sort.Direction dir = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        int size = Math.max(1, Math.min(limit, 50));
        return employees.findAll(PageRequest.of(0, size, Sort.by(dir, "annualSalaryUsd", "id")))
                .getContent().stream().map(EmployeeResponse::from).toList();
    }
}
