package com.acme.salary.employee;

import com.acme.salary.common.DuplicateEmailException;
import com.acme.salary.common.NotFoundException;
import com.acme.salary.common.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {
    private final EmployeeRepository repo;

    public EmployeeService(EmployeeRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public PageResponse<EmployeeResponse> search(EmployeeFilter filter, Pageable pageable) {
        return PageResponse.from(repo.findAll(EmployeeSpecs.from(filter), pageable).map(EmployeeResponse::from));
    }

    @Transactional(readOnly = true)
    public FilterOptions filterOptions() {
        return new FilterOptions(repo.distinctCountries(), repo.distinctDepartments(), repo.distinctJobTitles());
    }

    @Transactional(readOnly = true)
    public EmployeeResponse get(Long id) {
        return EmployeeResponse.from(find(id));
    }

    @Transactional
    public EmployeeResponse create(EmployeeRequest r) {
        if (repo.existsByEmail(r.email())) throw new DuplicateEmailException(r.email());
        return EmployeeResponse.from(repo.save(apply(new Employee(), r)));
    }

    @Transactional
    public EmployeeResponse update(Long id, EmployeeRequest r) {
        Employee e = find(id);
        if (repo.existsByEmailAndIdNot(r.email(), id)) throw new DuplicateEmailException(r.email());
        return EmployeeResponse.from(repo.save(apply(e, r)));
    }

    @Transactional
    public void delete(Long id) {
        repo.delete(find(id));
    }

    private Employee find(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Employee " + id + " not found"));
    }

    private Employee apply(Employee e, EmployeeRequest r) {
        String currency = r.currency().toUpperCase();
        e.setFullName(r.fullName().trim());
        e.setEmail(r.email().trim().toLowerCase());
        e.setJobTitle(r.jobTitle().trim());
        e.setDepartment(r.department().trim());
        e.setCountry(r.country().trim());
        e.setCurrency(currency);
        e.setAnnualSalary(r.annualSalary());
        e.setAnnualSalaryUsd(FxRates.toUsd(r.annualSalary(), currency));
        e.setHireDate(r.hireDate());
        return e;
    }
}
