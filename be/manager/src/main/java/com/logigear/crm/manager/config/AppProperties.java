package com.logigear.crm.manager.config;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConfigurationProperties(prefix = "crm")
@Getter
@Setter
public class AppProperties {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper;
    }

    /**
     * JWT token generation related properties
     */
    private Jwt jwt;

    /**
     * Properties related to JWT token generation
     *
     * @author Sanjay Patel
     */
    @Getter
    @Setter
    public static class Jwt {

        /**
         * Secret for signing JWT
         */
        private String secret;

        /**
         * Default expiration milliseconds
         */
        private long expirationMillis = 864000000L; // 10 days

        /**
         * Expiration milliseconds for short-lived tokens and cookies
         */
        private int shortLivedMillis = 120000; // Two minutes
    }
}