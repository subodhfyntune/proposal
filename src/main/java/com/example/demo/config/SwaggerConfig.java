package com.example.demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
	 @Bean
	    public OpenAPI openAPI() {
	        return new OpenAPI()
	            .info(new Info().title("Health API").version("v1"));
	    }
	}
//@Configuration
//public class OpenApiConfig {
//
//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
//            .components(new Components()
//                .addSecuritySchemes("bearerAuth",
//                    new SecurityScheme()
//                        .name("Authorization")
//                        .type(SecurityScheme.Type.HTTP)
//                        .scheme("bearer")
//                        .bearerFormat("JWT")
//                )
//            );
//    }
//}

