# Salary Management: Requirements

## Goal
Let ACME's HR Manager manage salary data for ~10,000 employees across several countries in a web app
(replacing Excel), and answer "how do we pay people?".

## Persona
HR Manager. Single role, full access.

## In scope
1. Employee CRUD: name, email, job title, department, country, currency, annual salary, hire date.
2. Search, filter (country, department, job title), sort, server-side pagination.
3. Salary insights: min / max / average / median by country, department and job title; org-wide summary;
   top and bottom earners.
4. Multi-currency: salary kept in local currency, also stored in USD so countries can be compared fairly.
5. Seed script: 10,000 deterministic employees.

## Out of scope, and why
- **Authentication / roles**: one persona; adds work without adding insight.
- **Payroll, tax, bonuses, equity**: a different problem (total compensation), not base-salary management.
- **Live FX rates**: needs an external dependency; a static table is enough to compare countries.
- **Salary history / audit trail**: valuable for v2, but the stated problem is about current pay.
- **Excel import UI**: the seed script covers loading data for this exercise.
- **Pay-gap / outlier detection**: promising next feature, left out to keep the scope small and correct.

## Assumptions
- One current annual salary per employee.
- Salaries are whole currency units (no fractions).
- FX rates are fixed and relative to USD.

## Success criteria
- Employee list loads fast with 10,000 rows (server-side pagination).
- Insight numbers come from SQL aggregation, not from loading everything into memory.
- Core logic is covered by fast, deterministic unit tests.
