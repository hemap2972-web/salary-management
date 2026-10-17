package com.acme.salary.insights;

import com.acme.salary.employee.EmployeeResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/insights")
public class InsightsController {
    private final InsightsService service;

    public InsightsController(InsightsService service) {
        this.service = service;
    }

    @GetMapping("/summary")
    SalarySummary summary() {
        return service.summary();
    }

    @GetMapping("/by/{dimension}")
    List<GroupStat> by(@PathVariable String dimension) {
        return service.by(Dimension.parse(dimension));
    }

    @GetMapping("/earners")
    List<EmployeeResponse> earners(@RequestParam(defaultValue = "desc") String order,
                                   @RequestParam(defaultValue = "10") int limit) {
        return service.earners(order, limit);
    }
}
