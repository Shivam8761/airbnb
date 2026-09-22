package com.shivam.airBnb.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI staySphereOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("StaySphere — Hotel & Room Booking API")
                        .description("RESTful backend API for StaySphere hotel and room booking platform featuring JWT authentication, role-based authorization, hotel/room inventory management, dynamic pricing strategies, and Stripe payment integration.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Shivam Vishwakarma")
                                .url("https://github.com/shivam8761")))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth",
                                new SecurityScheme()
                                        .name("BearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
