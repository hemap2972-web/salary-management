# Salary Management

Web software for ACME's HR Manager to manage salaries of ~10,000 employees across countries
and answer questions about how the organisation pays people.

**Status:** backend done (CRUD, search/filter/pagination, seed, insights, tests). Angular UI, Docker/deploy
and demo video are still to do.

## Run
Needs JDK 21 or newer.

```
./mvnw spring-boot:run        (Windows: .\mvnw.cmd spring-boot:run)
```
On first start it creates `salary.db` and seeds 10,000 employees. API at http://localhost:8080.

## Test
```
./mvnw test
```

## Try the API
```
curl "http://localhost:8080/api/employees?country=India&page=0&size=5&sort=annualSalaryUsd,desc"
curl http://localhost:8080/api/insights/summary
curl http://localhost:8080/api/insights/by/country
curl "http://localhost:8080/api/insights/earners?order=desc&limit=5"
```

## Docs
- [Requirements](docs/requirements.md)
- [Design](docs/design.md)
- [Trade-offs](docs/tradeoffs.md)
- [Performance](docs/performance.md)
- [AI prompts](docs/ai-prompts.md)
