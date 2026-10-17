package com.acme.salary.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.acme.salary.common.BadRequestException;
import com.acme.salary.common.DuplicateEmailException;
import com.acme.salary.common.NotFoundException;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock EmployeeRepository repo;
    @InjectMocks EmployeeService service;

    private EmployeeRequest request(String email, String currency, long salary) {
        return new EmployeeRequest("Asha Rao", email, "Software Engineer", "Engineering", "India",
                currency, salary, LocalDate.of(2022, 6, 1));
    }

    @Test
    void shouldRejectDuplicateEmailOnCreate() {
        when(repo.existsByEmail("asha@acme.com")).thenReturn(true);
        assertThrows(DuplicateEmailException.class,
                () -> service.create(request("asha@acme.com", "INR", 1_800_000)));
        verify(repo, never()).save(any());
    }

    @Test
    void shouldUppercaseCurrencyAndStoreUsdValueWhenCreating() {
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));
        EmployeeResponse res = service.create(request("asha@acme.com", "inr", 8_300_000));
        assertEquals("INR", res.currency());
        assertEquals(100_000, res.annualSalaryUsd());
    }

    @Test
    void shouldRejectUnsupportedCurrencyWithoutSaving() {
        assertThrows(BadRequestException.class,
                () -> service.create(request("asha@acme.com", "XXX", 1000)));
        verify(repo, never()).save(any());
    }

    @Test
    void shouldThrowNotFoundWhenDeletingMissingEmployee() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.delete(99L));
    }

    @Test
    void shouldRejectUpdateThatReusesAnotherEmployeesEmail() {
        when(repo.findById(1L)).thenReturn(Optional.of(new Employee()));
        when(repo.existsByEmailAndIdNot("taken@acme.com", 1L)).thenReturn(true);
        assertThrows(DuplicateEmailException.class,
                () -> service.update(1L, request("taken@acme.com", "INR", 1000)));
    }
}
