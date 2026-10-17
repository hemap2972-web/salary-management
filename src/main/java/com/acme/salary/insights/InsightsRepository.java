package com.acme.salary.insights;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;

/** Aggregations run in the database. The field name comes from the Dimension enum, never from user text. */
@Repository
public class InsightsRepository {

    @PersistenceContext
    private EntityManager em;

    /** Rows of [name, count, min, max, avg]. */
    public List<Object[]> aggregates(Dimension d) {
        String f = d.field();
        return em.createQuery("select e." + f + ", count(e), min(e.annualSalaryUsd), max(e.annualSalaryUsd), "
                + "avg(e.annualSalaryUsd) from Employee e group by e." + f + " order by e." + f, Object[].class)
                .getResultList();
    }

    /** Rows of [name, salaryUsd] sorted, used to compute medians (SQLite has no median function). */
    public List<Object[]> orderedSalaries(Dimension d) {
        String f = d.field();
        return em.createQuery("select e." + f + ", e.annualSalaryUsd from Employee e order by e." + f
                + ", e.annualSalaryUsd", Object[].class).getResultList();
    }

    public List<Long> allSalariesSorted() {
        return em.createQuery("select e.annualSalaryUsd from Employee e order by e.annualSalaryUsd", Long.class)
                .getResultList();
    }
}
