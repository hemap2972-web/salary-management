CREATE TABLE IF NOT EXISTS employee (
  id                INTEGER PRIMARY KEY AUTOINCREMENT,
  full_name         TEXT    NOT NULL,
  email             TEXT    NOT NULL UNIQUE,
  job_title         TEXT    NOT NULL,
  department        TEXT    NOT NULL,
  country           TEXT    NOT NULL,
  currency          TEXT    NOT NULL,
  annual_salary     INTEGER NOT NULL,
  annual_salary_usd INTEGER NOT NULL,
  hire_date         TEXT    NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_emp_country    ON employee(country);
CREATE INDEX IF NOT EXISTS idx_emp_department ON employee(department);
CREATE INDEX IF NOT EXISTS idx_emp_job_title  ON employee(job_title);
CREATE INDEX IF NOT EXISTS idx_emp_salary_usd ON employee(annual_salary_usd);
