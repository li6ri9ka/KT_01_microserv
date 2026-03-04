package com.example.KT_01.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class TodoConsoleClient {

    private static final String BASE_URL = "http://localhost:8080/tasks";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Scanner scanner;

    public TodoConsoleClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        new TodoConsoleClient().run();
    }

    private void run() {
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> listTasks();
                    case "2" -> addTask();
                    case "3" -> updateTask();
                    case "4" -> deleteTask();
                    case "0" -> {
                        System.out.println("Exiting client.");
                        return;
                    }
                    default -> System.out.println("Unknown action.");
                }
            } catch (Exception ex) {
                System.out.println("Request failed: " + ex.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("=== To-Do Client ===");
        System.out.println("1 - Show tasks");
        System.out.println("2 - Add task");
        System.out.println("3 - Update task");
        System.out.println("4 - Delete task");
        System.out.println("0 - Exit");
        System.out.print("Choose: ");
    }

    private void listTasks() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.out.println("Error: " + response.statusCode() + " -> " + response.body());
            return;
        }

        List<TaskDto> tasks = objectMapper.readValue(
                response.body(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, TaskDto.class));

        if (tasks.isEmpty()) {
            System.out.println("Task list is empty.");
            return;
        }

        System.out.println("Tasks:");
        for (TaskDto task : tasks) {
            String status = task.completed ? "DONE" : "TODO";
            System.out.printf("%d | %s | %s%n", task.id, status, task.title);
        }
    }

    private void addTask() throws IOException, InterruptedException {
        System.out.print("Title: ");
        String title = scanner.nextLine();

        String payload = objectMapper.writeValueAsString(new CreateTaskRequest(title));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response: " + response.statusCode() + " -> " + response.body());
    }

    private void updateTask() throws IOException, InterruptedException {
        Long id = readLong("Task ID: ");
        if (id == null) {
            return;
        }

        System.out.print("New title (leave empty to keep unchanged): ");
        String title = scanner.nextLine();

        System.out.print("Completed? (y/n/skip): ");
        String completedRaw = scanner.nextLine().trim().toLowerCase();

        Boolean completed = switch (completedRaw) {
            case "y", "yes" -> true;
            case "n", "no" -> false;
            default -> null;
        };

        UpdateTaskRequest requestPayload = new UpdateTaskRequest(
                title.isBlank() ? null : title,
                completed);

        String payload = objectMapper.writeValueAsString(requestPayload);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response: " + response.statusCode() + " -> " + response.body());
    }

    private void deleteTask() throws IOException, InterruptedException {
        Long id = readLong("Task ID: ");
        if (id == null) {
            return;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response: " + response.statusCode());
    }

    private Long readLong(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();

        try {
            return Long.parseLong(input);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid numeric value.");
            return null;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class TaskDto {
        public Long id;
        public String title;
        public boolean completed;
    }

    private record CreateTaskRequest(String title) {
    }

    private record UpdateTaskRequest(String title, Boolean completed) {
    }
}
