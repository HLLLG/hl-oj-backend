# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./mvnw clean package -DskipTests

# Run
./mvnw spring-boot:run

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=HlOjBackendApplicationTests
```

## Stack

- **Java 21**, Spring Boot 3.5.11
- **MyBatis-Plus 3.5.15** for ORM (uses `IService`/`ServiceImpl` pattern, XML mappers in `src/main/resources/mapper/`)
- **MySQL** database `hl_oj` (localhost:3306, default credentials in `application.yaml`)
- **Knife4j / SpringDoc OpenAPI 3** for API docs — UI at `http://localhost:8123/api/doc.html`
- Server runs on port `8123` with context path `/api`

## Architecture

Layered Spring MVC: `controller → service (interface + impl) → mapper → DB`

**Response pattern** — all endpoints return `BaseResponse<T>`:
- Success: `ResultsUtils.success(data)` → `{code: 0, message: "ok", data: ...}`
- Error: `ResultsUtils.error(ErrorCode.XXX)` or throw `BusinessException`
- `GlobalExceptionHandler` catches `BusinessException` and `RuntimeException` and maps them to `BaseResponse`

**Error handling** — use `ThrowUtils.throwIf(condition, ErrorCode.XXX)` for guard clauses instead of explicit `if/throw` blocks.

**Common DTOs** in `common/`:
- `PageRequest` — base class for paginated list requests (`current`, `pageSize`, `sortField`, `sortOrder`)
- `DeleteRequest` — generic delete by `id`

**Entity conventions** — all entities use `@TableId(type = IdType.ASSIGN_ID)` (snowflake ID), have `createTime`, `updateTime`, `editTime`, and soft-delete via `isDelete` (tinyint).

## Database Setup

Run `sql/create_table.sql` to initialize the schema.
