# Trade-offs

- **SQLite**: zero setup and enough for 10k rows. Limits: one writer, so the connection pool is size 1;
  no median function. Postgres would be the production choice.
- **Median in Java**: min/max/avg/count are SQL aggregates. Median needs sorted values, so those are read
  (one column, 10k numbers) and the median computed in the service. Fine at this size; at millions of rows
  use a window function in Postgres.
- **USD stored at write time**: makes cross-country SQL simple and fast. Cost: if FX rates change, old rows
  keep the old rate. A rates table with effective dates would fix that.
- **Static FX rates**: no external dependency, but not real-time.
- **JDBC batch seeding**: JPA cannot batch inserts with IDENTITY ids, so the seeder uses JDBC batches.
- **No auth**: single persona, called out in requirements as a deliberate omission.
