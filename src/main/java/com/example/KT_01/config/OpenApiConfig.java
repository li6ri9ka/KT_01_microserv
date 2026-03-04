package com.example.KT_01.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI todoOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("KT_01 User Management API")
                .description("REST API for managing users with JPA/Flyway and tasks in database")
                .version("1.0.0")
                .contact(new Contact()
                        .name("KT_01 Team")
                        .email("support@example.com")
                        .url("https://example.com")));
    }
}
