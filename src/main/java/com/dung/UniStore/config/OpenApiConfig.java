package com.dung.UniStore.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@OpenAPIDefinition (
        info = @Info (
                title = "My Awesome API",
                version = "2.0.0",
                description = "Description of my Awesome API"
        ),
        servers = {
                @Server(url = "http://localhost:8081",description = "Local development server"),
                @Server(url = "http://45.117.179.16:8088",description = "Product Server")
        }
)
@SecurityScheme(
        name = "bearer-key",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenApiConfig {

}
