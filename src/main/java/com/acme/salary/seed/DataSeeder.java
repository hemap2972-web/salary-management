package com.acme.salary.seed;

import com.acme.salary.employee.Employee;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

/** Seeds the database on startup, only when it is empty. Uses JDBC batches because JPA cannot batch IDENTITY ids. */
@Component
public class DataSeeder implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    private static final long SEED = 42L;
    private static final int BATCH = 500;
    private static final String INSERT = "insert into employee (full_name, email, job_title, department, country, "
            + "currency, annual_salary, annual_salary_usd, hire_date) values (?,?,?,?,?,?,?,?,?)";

    private final JdbcTemplate jdbc;
    private final TransactionTemplate tx;
    private final boolean enabled;
    private final int count;

    public DataSeeder(JdbcTemplate jdbc, TransactionTemplate tx,
                      @Value("${app.seed.enabled:true}") boolean enabled,
                      @Value("${app.seed.count:10000}") int count) {
        this.jdbc = jdbc;
        this.tx = tx;
        this.enabled = enabled;
        this.count = count;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!enabled) return;
        Long existing = jdbc.queryForObject("select count(*) from employee", Long.class);
        if (existing != null && existing > 0) {
            log.info("Seed skipped: {} employees already present", existing);
            return;
        }
        List<Employee> rows = new SeedDataGenerator().generate(count, SEED);
        tx.executeWithoutResult(status -> {
            for (int from = 0; from < rows.size(); from += BATCH) {
                List<Employee> chunk = rows.subList(from, Math.min(from + BATCH, rows.size()));
                jdbc.batchUpdate(INSERT, chunk, chunk.size(), (ps, e) -> {
                    ps.setString(1, e.getFullName());
                    ps.setString(2, e.getEmail());
                    ps.setString(3, e.getJobTitle());
                    ps.setString(4, e.getDepartment());
                    ps.setString(5, e.getCountry());
                    ps.setString(6, e.getCurrency());
                    ps.setLong(7, e.getAnnualSalary());
                    ps.setLong(8, e.getAnnualSalaryUsd());
                    ps.setString(9, e.getHireDate().toString());
                });
            }
        });
        log.info("Seeded {} employees", rows.size());
    }
}
