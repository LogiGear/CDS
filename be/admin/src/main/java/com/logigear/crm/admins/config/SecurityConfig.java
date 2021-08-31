package com.logigear.crm.admins.config;

import com.logigear.crm.admins.security.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] ALLOWED_PATHS = { "/", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg",
            "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js", "/actuator/**" };

    public static final String[] ALLOWED_SWAGGER = { "/authenticate", "/api-docs/**", "/swagger-ui/**",
            "/swagger-ui.html", "/v3/api-docs/**", "/v2/api-docs", "/swagger-resources/**" };

    private static final String ALLOWED_ADMINS_PATH = "/admin/api/admins/**";

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests().antMatchers(ALLOWED_PATHS).permitAll()
                .antMatchers(ALLOWED_ADMINS_PATH).permitAll()
                .antMatchers(ALLOWED_SWAGGER).permitAll().anyRequest().authenticated();
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
