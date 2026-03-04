package com.example.KT_01.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User entity")
public class User {

    @Schema(description = "Unique identifier", example = "1")
    private Long id;

    @Schema(description = "User name", example = "Ivan Ivanov")
    private String name;

    @Schema(description = "User email", example = "ivan@example.com")
    private String email;

    public User() {
    }

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
