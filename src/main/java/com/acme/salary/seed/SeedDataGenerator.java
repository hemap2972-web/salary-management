package com.acme.salary.seed;

import com.acme.salary.employee.Employee;
import com.acme.salary.employee.FxRates;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Pure, deterministic generator: the same seed always produces the same employees. */
public class SeedDataGenerator {

    private record Country(String name, String currency, double costFactor, int weight) {}

    private record Role(String title, String department, int minUsd, int maxUsd) {}

    private static final List<Country> COUNTRIES = List.of(
            new Country("India", "INR", 0.35, 30),
            new Country("United States", "USD", 1.00, 25),
            new Country("United Kingdom", "GBP", 0.85, 10),
            new Country("Germany", "EUR", 0.85, 10),
            new Country("Canada", "CAD", 0.90, 8),
            new Country("Australia", "AUD", 0.90, 7),
            new Country("Singapore", "SGD", 0.95, 5),
            new Country("Japan", "JPY", 0.80, 5));

    private static final List<Role> ROLES = List.of(
            new Role("Software Engineer", "Engineering", 60_000, 120_000),
            new Role("Senior Software Engineer", "Engineering", 100_000, 170_000),
            new Role("Engineering Manager", "Engineering", 130_000, 200_000),
            new Role("QA Engineer", "Engineering", 45_000, 90_000),
            new Role("DevOps Engineer", "Engineering", 70_000, 130_000),
            new Role("Product Manager", "Product", 90_000, 160_000),
            new Role("Designer", "Product", 55_000, 110_000),
            new Role("Sales Executive", "Sales", 40_000, 90_000),
            new Role("Sales Manager", "Sales", 80_000, 150_000),
            new Role("HR Specialist", "HR", 40_000, 80_000),
            new Role("Recruiter", "HR", 40_000, 85_000),
            new Role("Accountant", "Finance", 45_000, 90_000),
            new Role("Financial Analyst", "Finance", 55_000, 110_000),
            new Role("Operations Analyst", "Operations", 45_000, 95_000),
            new Role("Support Specialist", "Operations", 30_000, 60_000));

    private static final List<String> FIRST = List.of("Asha", "Rahul", "Priya", "Arjun", "Meera", "Vikram", "Sana",
            "Kiran", "Neha", "Rohan", "Emma", "Liam", "Olivia", "Noah", "Sophia", "James", "Mia", "Lucas", "Hana",
            "Kenji", "Aiko", "Ravi", "Lena", "Hans", "Chloe", "Ethan", "Grace", "Daniel", "Zoe", "Omar");

    private static final List<String> LAST = List.of("Rao", "Sharma", "Patel", "Iyer", "Nair", "Reddy", "Khan",
            "Singh", "Gupta", "Mehta", "Smith", "Johnson", "Brown", "Taylor", "Wilson", "Clark", "Walker", "Young",
            "Tanaka", "Suzuki", "Sato", "Muller", "Schmidt", "Fischer", "Tan", "Lim", "Wong", "Evans", "Hughes",
            "Morgan");

    private static final LocalDate EARLIEST = LocalDate.of(2015, 1, 1);
    private static final LocalDate LATEST = LocalDate.of(2026, 6, 30);

    public List<Employee> generate(int count, long seed) {
        Random rnd = new Random(seed);
        int totalWeight = COUNTRIES.stream().mapToInt(Country::weight).sum();
        long dayRange = LATEST.toEpochDay() - EARLIEST.toEpochDay();
        List<Employee> out = new ArrayList<>(count);

        for (int i = 1; i <= count; i++) {
            Country country = pickCountry(rnd, totalWeight);
            Role role = ROLES.get(rnd.nextInt(ROLES.size()));
            String first = FIRST.get(rnd.nextInt(FIRST.size()));
            String last = LAST.get(rnd.nextInt(LAST.size()));

            // Average of two uniform draws gives a bell-ish shape, so most people sit mid-band.
            double position = (rnd.nextDouble() + rnd.nextDouble()) / 2.0;
            double baseUsd = (role.minUsd() + position * (role.maxUsd() - role.minUsd())) * country.costFactor();
            long local = Math.max(100, Math.round(FxRates.fromUsd(baseUsd, country.currency()) / 100.0) * 100);

            Employee e = new Employee();
            e.setFullName(first + " " + last);
            e.setEmail((first + "." + last + i + "@acme.com").toLowerCase());
            e.setJobTitle(role.title());
            e.setDepartment(role.department());
            e.setCountry(country.name());
            e.setCurrency(country.currency());
            e.setAnnualSalary(local);
            e.setAnnualSalaryUsd(FxRates.toUsd(local, country.currency()));
            e.setHireDate(EARLIEST.plusDays((long) (rnd.nextDouble() * dayRange)));
            out.add(e);
        }
        return out;
    }

    private Country pickCountry(Random rnd, int totalWeight) {
        int roll = rnd.nextInt(totalWeight);
        for (Country c : COUNTRIES) {
            roll -= c.weight();
            if (roll < 0) return c;
        }
        return COUNTRIES.get(0);
    }
}
