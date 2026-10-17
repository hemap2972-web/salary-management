package com.acme.salary.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record EmployeeRequest(
        @NotBlank String fullName,
        @NotBlank @Email String email,
        @NotBlank String jobTitle,
        @NotBlank String department,
        @NotBlank String country,
        @NotBlank @Size(min = 3, max = 3) String currency,
        @Positive long annualSalary,
        @NotNull @PastOrPresent LocalDate hireDate) {
}
