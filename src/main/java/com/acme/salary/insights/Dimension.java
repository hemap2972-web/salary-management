package com.acme.salary.insights;

import com.acme.salary.common.BadRequestException;

/** The only fields insights may group by. Whitelisted because the field name is placed into JPQL. */
public enum Dimension {
    COUNTRY("country"),
    DEPARTMENT("department"),
    JOBTITLE("jobTitle");

    private final String field;

    Dimension(String field) {
        this.field = field;
    }

    public String field() {
        return field;
    }

    public static Dimension parse(String raw) {
        String key = raw == null ? "" : raw.replace("-", "").replace("_", "").toUpperCase();
        for (Dimension d : values()) {
            if (d.name().equals(key)) return d;
        }
        throw new BadRequestException("Unknown dimension: " + raw + " (use country, department or job-title)");
    }
}
