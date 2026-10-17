package com.acme.salary.employee;

import java.time.LocalDate;

public record EmployeeResponse(Long id, String fullName, String email, String jobTitle, String department,
                               String country, String currency, long annualSalary, long annualSalaryUsd,
                               LocalDate hireDate) {
    public static EmployeeResponse from(Employee e) {
        return new EmployeeResponse(e.getId(), e.getFullName(), e.getEmail(), e.getJobTitle(), e.getDepartment(),
                e.getCountry(), e.getCurrency(), e.getAnnualSalary(), e.getAnnualSalaryUsd(), e.getHireDate());
    }
}
