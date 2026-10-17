package com.acme.salary.insights;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.acme.salary.common.BadRequestException;
import com.acme.salary.employee.Employee;
import com.acme.salary.employee.EmployeeRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({InsightsService.class, InsightsRepository.class})
class InsightsServiceTest {
    @Autowired EmployeeRepository repo;
    @Autowired InsightsService service;

    @BeforeEach
    void setUp() {
        repo.saveAll(List.of(
                emp("a@x.com", "India", 10), emp("b@x.com", "India", 30), emp("c@x.com", "India", 20),
                emp("d@x.com", "Germany", 40), emp("e@x.com", "Germany", 60)));
    }

    private Employee emp(String email, String country, long usd) {
        Employee e = new Employee();
        e.setFullName("N");
        e.setEmail(email);
        e.setJobTitle("Role");
        e.setDepartment("Dept");
        e.setCountry(country);
        e.setCurrency("USD");
        e.setAnnualSalary(usd);
        e.setAnnualSalaryUsd(usd);
        e.setHireDate(LocalDate.of(2020, 1, 1));
        return e;
    }

    @Test
    void shouldComputeStatsPerCountry() {
        List<GroupStat> stats = service.by(Dimension.COUNTRY);
        GroupStat india = stats.stream().filter(s -> s.name().equals("India")).findFirst().orElseThrow();
        assertEquals(3, india.count());
        assertEquals(10, india.min());
        assertEquals(30, india.max());
        assertEquals(20, india.avg());
        assertEquals(20, india.median());
    }

    @Test
    void shouldComputeMedianOfEvenGroupAsAverageOfMiddleTwo() {
        GroupStat germany = service.by(Dimension.COUNTRY).stream()
                .filter(s -> s.name().equals("Germany")).findFirst().orElseThrow();
        assertEquals(50, germany.median());
    }

    @Test
    void shouldSummariseWholeOrganisation() {
        SalarySummary s = service.summary();
        assertEquals(5, s.headcount());
        assertEquals(160, s.totalPayroll());
        assertEquals(30, s.median());
    }

    @Test
    void shouldReturnTopEarnersFirst() {
        assertEquals(60, service.earners("desc", 1).get(0).annualSalaryUsd());
    }

    @Test
    void shouldRejectUnknownDimension() {
        assertThrows(BadRequestException.class, () -> Dimension.parse("salary"));
    }
}
