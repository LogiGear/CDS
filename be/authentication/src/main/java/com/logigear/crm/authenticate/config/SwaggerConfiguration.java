package com.logigear.crm.authenticate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.SecurityContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
@Configuration
public class SwaggerConfiguration {
    private static final String API_NAME = "CDS API DOCUMENTATION";
    private static final String API_VERSION = "v1.0.0";
    private static final String API_DESCRIPTION = "API Documentation for Employees CRUD";
    private static final String API_TOS = "Terms of service";
    private static final String API_LICENSE_OF = "CDS API DOCUMENTATION";
    private static final String API_LICENSE_URL = "CDS API DOCUMENTATION";
    private static final Contact API_AUTHOR_CONTACT
            = new Contact("BanhsBao", "www.logigear.com", "bao.chau.huynh@logigear.com");
    private static final String BASE_API_PACKAGE = "com.logigear.crm.authenticate.controller";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select().apis(RequestHandlerSelectors.basePackage(BASE_API_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(API_NAME, API_DESCRIPTION, API_VERSION, API_TOS, API_AUTHOR_CONTACT, API_LICENSE_OF,
                API_LICENSE_URL, Collections.emptyList());
    }
    private ApiKey apiKey() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }

}
