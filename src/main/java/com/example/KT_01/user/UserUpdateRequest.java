package com.example.KT_01.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload for updating user")
public class UserUpdateRequest {

    @NotBlank(message = "name must not be blank")
    @Schema(description = "Updated user name", example = "Petr Petrov")
    private String name;

    @NotBlank(message = "email must not be blank")
    @Email(message = "email must be valid")
    @Schema(description = "Updated user email", example = "petr@example.com")
    private String email;

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
