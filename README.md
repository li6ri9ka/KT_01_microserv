# KT_01 API Project

Проект содержит два модуля API:
- `Task API` (`/tasks`) с хранением в БД
- `User API` (`/api/users`) на Spring Data JPA

Для `User API` добавлены миграции Flyway.

## Технологии

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- H2 / PostgreSQL
- Flyway
- Swagger/OpenAPI (springdoc)
- Docker, Docker Compose

## Запуск локально (без Docker)

```bash
./gradlew bootRun
```

## Запуск через Docker Compose

```bash
docker compose up --build -d
```

Остановить:

```bash
docker compose down
```

Остановить и удалить volume PostgreSQL:

```bash
docker compose down -v
```

## Swagger UI

- `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## User API

- `GET /api/users` - получить всех пользователей
- `GET /api/users/{id}` - получить пользователя по ID
- `POST /api/users` - создать пользователя
- `PUT /api/users/{id}` - обновить пользователя
- `DELETE /api/users/{id}` - удалить пользователя

Поля пользователя:
- `id: Long`
- `name: String`
- `email: String`
- `age: Integer`

Пример `POST /api/users`:

```json
{
  "name": "Ivan Ivanov",
  "email": "ivan@example.com",
  "age": 22
}
```

## Flyway

Миграция:
- `src/main/resources/db/migration/V1__Create_User_Table.sql`

Создаёт таблицу `users`:
- `id` (PK)
- `name`
- `email` (UNIQUE)
- `age`

## Тесты

```bash
./gradlew test
```
