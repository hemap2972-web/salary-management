package com.acme.salary.common;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) { super("Email already exists: " + email); }
}
