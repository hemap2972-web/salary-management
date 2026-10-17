package com.acme.salary.insights;

/** All money values are annual salaries in USD. */
public record GroupStat(String name, long count, long min, long max, long avg, long median) {
}
