package com.logview.logview.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI logViewOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LogView API")
                        .description("API para ingestão e análise de arquivos de log")
                        .version("0.0.1"));
    }
}
