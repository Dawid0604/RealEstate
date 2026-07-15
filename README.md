# EstateHub — Real Estate Listings API

> REST API dla portalu ogłoszeń nieruchomości. Backend wzorowany na OtoDom/Morizon.pl.
> Projekt portfolio demonstrający Hexagonal Architecture, DDD, CQRS i pełny observability stack.

> ⚠️ **Aktywny development odbywa się na branchu [`dev`](https://github.com/Dawid0604/RealEstate/tree/dev).**
> `main` jest zarezerwowany dla finalnego MVP.
> Aktualny postęp: [Project board](https://github.com/users/Dawid0604/projects/11/views/1) · [Aktywny PR](https://github.com/Dawid0604/RealEstate/pull/12)

---

## Stack technologiczny

| Warstwa | Technologie |
|---|---|
| **Core** | Java 21 · Spring Boot 4.0.3 (JPA, Security, Actuator)  |
| **Architektura** | Hexagonal Architecture · Modular Monolith · Multi-module Maven |
| **Wzorce** | DDD · CQRS · Domain Events |
| **Baza danych** | PostgreSQL 18 · Flyway · Spring Data JPA Specifications |
| **Bezpieczeństwo** | JWT Bearer + Refresh Token · Spring Security stateless |
| **Observability** | OpenTelemetry + Grafana Tempo · Loki + Loki4j · Micrometer + Prometheus + Grafana |
| **Testy** | JUnit 6 · Mockito · Testcontainers · ArchUnit |
| **DevOps** | Docker · docker-compose · GitHub Actions |
