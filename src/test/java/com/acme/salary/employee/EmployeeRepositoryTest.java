package com.acme.salary.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@DataJpaTest
class EmployeeRepositoryTest {
    @Autowired EmployeeRepository repo;

    @BeforeEach
    void setUp() {
        repo.saveAll(List.of(
                emp("Asha Rao", "asha@acme.com", "Engineering", "India", 10),
                emp("Rahul Sharma", "rahul@acme.com", "Engineering", "India", 30),
                emp("Emma Smith", "emma@acme.com", "Sales", "United States", 50),
                emp("Liam Brown", "liam@acme.com", "Sales", "United States", 70),
                emp("Priya Nair", "priya@acme.com", "HR", "India", 20)));
    }

    private Employee emp(String name, String email, String dept, String country, long usd) {
        Employee e = new Employee();
        e.setFullName(name);
        e.setEmail(email);
        e.setJobTitle("Role");
        e.setDepartment(dept);
        e.setCountry(country);
        e.setCurrency("USD");
        e.setAnnualSalary(usd);
        e.setAnnualSalaryUsd(usd);
        e.setHireDate(LocalDate.of(2020, 1, 1));
        return e;
    }

    @Test
    void shouldFilterByCountry() {
        Page<Employee> page = repo.findAll(EmployeeSpecs.from(new EmployeeFilter(null, "India", null, null)),
                PageRequest.of(0, 10));
        assertEquals(3, page.getTotalElements());
    }

    @Test
    void shouldSearchNameCaseInsensitively() {
        Page<Employee> page = repo.findAll(EmployeeSpecs.from(new EmployeeFilter("ASHA", null, null, null)),
                PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
    }

    @Test
    void shouldCombineFilters() {
        Page<Employee> page = repo.findAll(
                EmployeeSpecs.from(new EmployeeFilter(null, "India", "Engineering", null)), PageRequest.of(0, 10));
        assertEquals(2, page.getTotalElements());
    }

    @Test
    void shouldPaginateAndSortBySalaryDescending() {
        Page<Employee> page = repo.findAll(EmployeeSpecs.from(new EmployeeFilter(null, null, null, null)),
                PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "annualSalaryUsd")));
        assertEquals(3, page.getTotalPages());
        assertEquals(70, page.getContent().get(0).getAnnualSalaryUsd());
    }
}
