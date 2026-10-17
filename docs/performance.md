# Performance notes

- Employee list is paginated on the server (max page size 100). The browser never receives 10k rows.
- Indexes on country, department, job_title and annual_salary_usd support filters, grouping and earner sorting.
- Group statistics use SQL `GROUP BY`; only the salary column is read for medians.
- Seeding inserts in JDBC batches of 500 inside one transaction, and only when the table is empty.
- Search uses `LIKE '%text%'` on name and email, which cannot use an index. Acceptable at 10k rows;
  SQLite FTS would be the next step.
