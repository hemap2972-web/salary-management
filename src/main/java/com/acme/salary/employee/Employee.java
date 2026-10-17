package com.acme.salary.employee;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;
    private String jobTitle;
    private String department;
    private String country;
    private String currency;
    /** Annual salary in the employee's local currency, whole units. */
    private long annualSalary;
    /** Same salary converted to USD at write time, used for cross-country comparison. */
    private long annualSalaryUsd;
    @Convert(converter = LocalDateStringConverter.class)
    private LocalDate hireDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public long getAnnualSalary() { return annualSalary; }
    public void setAnnualSalary(long annualSalary) { this.annualSalary = annualSalary; }
    public long getAnnualSalaryUsd() { return annualSalaryUsd; }
    public void setAnnualSalaryUsd(long annualSalaryUsd) { this.annualSalaryUsd = annualSalaryUsd; }
    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
}
