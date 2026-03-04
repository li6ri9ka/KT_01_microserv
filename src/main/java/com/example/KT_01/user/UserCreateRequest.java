package com.example.KT_01.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload for creating user")
public class UserCreateRequest {

    @NotBlank(message = "name must not be blank")
    @Schema(description = "User name", example = "Ivan Ivanov")
    private String name;

    @NotBlank(message = "email must not be blank")
    @Email(message = "email must be valid")
    @Schema(description = "User email", example = "ivan@example.com")
    private String email;

    @Min(value = 0, message = "age must be non-negative")
    @Schema(description = "User age", example = "22")
    private Integer age;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
