# KT_01 To-Do List (Client-Server)

Приложение реализует управление задачами по HTTP:
- сервер: Spring Boot REST API
- клиент: консольное Java-приложение на `HttpClient`

## Функциональность

Сервер поддерживает CRUD-операции:
- `GET /tasks` - получить список задач
- `POST /tasks` - создать задачу
- `PUT /tasks/{id}` - обновить задачу (title и/или completed)
- `DELETE /tasks/{id}` - удалить задачу

Модель задачи:
- `id` (`Long`)
- `title` (`String`)
- `completed` (`boolean`)

## Запуск

1. Запустить сервер:

```bash
./gradlew bootRun
```

2. В другом терминале запустить консольный клиент:

```bash
./gradlew runClient
```

## Примеры JSON

Создание задачи (`POST /tasks`):

```json
{
  "title": "Buy milk"
}
```

Обновление задачи (`PUT /tasks/{id}`):

```json
{
  "title": "Buy milk and bread",
  "completed": true
}
```

Можно передавать только одно поле (`title` или `completed`).

## Проверка

Запуск тестов:

```bash
./gradlew test
```

Тесты покрывают:
- создание и получение задач
- обновление задачи
- удаление задачи
- валидацию пустого `title`
- `404` для отсутствующей задачи
