package com.acme.salary.insights;

/** All money values are annual salaries in USD. */
public record SalarySummary(long headcount, long totalPayroll, long min, long max, long avg, long median) {
}
