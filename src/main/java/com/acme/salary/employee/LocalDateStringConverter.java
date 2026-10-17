package com.acme.salary.employee;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDate;

/** Stores dates as ISO text (yyyy-MM-dd). Avoids SQLite JDBC's date-as-milliseconds behaviour. */
@Converter
public class LocalDateStringConverter implements AttributeConverter<LocalDate, String> {
    @Override
    public String convertToDatabaseColumn(LocalDate d) { return d == null ? null : d.toString(); }

    @Override
    public LocalDate convertToEntityAttribute(String s) { return s == null ? null : LocalDate.parse(s); }
}
