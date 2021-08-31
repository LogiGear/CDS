package com.logigear.crm.template.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "crm")
@Getter
@Setter
public class AppProperties {

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