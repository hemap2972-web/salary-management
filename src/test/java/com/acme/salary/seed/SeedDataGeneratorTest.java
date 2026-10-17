package com.acme.salary.seed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.acme.salary.employee.Employee;
import com.acme.salary.employee.FxRates;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SeedDataGeneratorTest {
    private final SeedDataGenerator generator = new SeedDataGenerator();

    @Test
    void shouldProduceSameDataForSameSeed() {
        List<Employee> a = generator.generate(200, 42);
        List<Employee> b = generator.generate(200, 42);
        for (int i = 0; i < a.size(); i++) {
            assertEquals(a.get(i).getEmail(), b.get(i).getEmail());
            assertEquals(a.get(i).getAnnualSalary(), b.get(i).getAnnualSalary());
            assertEquals(a.get(i).getHireDate(), b.get(i).getHireDate());
        }
    }

    @Test
    void shouldProduceDifferentDataForDifferentSeed() {
        long sumA = generator.generate(100, 1).stream().mapToLong(Employee::getAnnualSalaryUsd).sum();
        long sumB = generator.generate(100, 2).stream().mapToLong(Employee::getAnnualSalaryUsd).sum();
        assertNotEquals(sumA, sumB);
    }

    @Test
    void shouldGenerateUniqueEmailsAndRequestedCount() {
        List<Employee> rows = generator.generate(10_000, 42);
        Set<String> emails = new HashSet<>();
        rows.forEach(e -> emails.add(e.getEmail()));
        assertEquals(10_000, rows.size());
        assertEquals(10_000, emails.size());
    }

    @Test
    void shouldOnlyUseSupportedCurrenciesAndPositiveSalaries() {
        for (Employee e : generator.generate(1_000, 42)) {
            assertTrue(FxRates.supported().contains(e.getCurrency()));
            assertTrue(e.getAnnualSalary() > 0);
            assertTrue(e.getAnnualSalaryUsd() > 0);
        }
    }
}
