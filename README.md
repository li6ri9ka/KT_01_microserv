# KT_01 API Project

Проект содержит два модуля API:
- `Task API` (`/tasks`) с хранением в H2
- `User API` (`/api/users`) с хранением в памяти (`ArrayList`)

Также интегрирован Swagger (OpenAPI) для документирования и тестирования.

## Запуск

```bash
./gradlew bootRun
```

## Swagger UI

- `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## User API

- `GET /api/users` - получить всех пользователей
- `GET /api/users/{id}` - получить пользователя по id
- `POST /api/users` - создать пользователя
- `PUT /api/users/{id}` - обновить пользователя
- `DELETE /api/users/{id}` - удалить пользователя

Поля пользователя:
- `id: Long`
- `name: String`
- `email: String`

Пример `POST /api/users`:

```json
{
  "name": "Ivan Ivanov",
  "email": "ivan@example.com"
}
```

Пример `PUT /api/users/1`:

```json
{
  "name": "Petr Petrov",
  "email": "petr@example.com"
}
```

## Task API

- `GET /tasks`
- `POST /tasks`
- `PUT /tasks/{id}`
- `DELETE /tasks/{id}`

## Тесты

```bash
./gradlew test
```
