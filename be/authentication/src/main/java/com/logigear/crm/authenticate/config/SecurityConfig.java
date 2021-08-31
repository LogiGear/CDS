package com.logigear.crm.authenticate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.logigear.crm.authenticate.security.CustomUserDetailsService;
import com.logigear.crm.authenticate.security.JwtAuthenticationEntryPoint;
import com.logigear.crm.authenticate.security.JwtAuthenticationFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String[] ALLOWED_PATH = { "/", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg",
			"/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js", "/actuator/**" };
	public static final String[] ALLOWED_SWAGGER = {"/authenticate", "/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/v2/api-docs",
			"/swagger-resources/**"};
	public static final String ALLOWED_AUTH_PATH = "/authentication/api/auth/**";
	public static final String ALLOWED_ADMINS_PATH = "/authentication/api/admins/**";

	private final CustomUserDetailsService customUserDetailsService;
	private final JwtAuthenticationEntryPoint unauthorizedHandler;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtAuthenticationEntryPoint unauthorizedHandler,
			JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.customUserDetailsService = customUserDetailsService;
		this.unauthorizedHandler = unauthorizedHandler;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	// This configure method is for LDAP Authentication
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.ldapAuthentication().userSearchFilter("(mail={0}*)").groupSearchBase("ou=groups")
				.contextSource(contextSource()).passwordCompare().passwordEncoder(passwordEncoder())
				.passwordAttribute("userPassword");
	}

	@Bean
	public DefaultSpringSecurityContextSource contextSource() {
		return new DefaultSpringSecurityContextSource(Collections.singletonList("ldap://localhost:10389"),
				"dc=logigear,dc=org");
	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers(ALLOWED_PATH).permitAll().antMatchers(ALLOWED_AUTH_PATH).permitAll()
				.antMatchers(ALLOWED_SWAGGER)
				.permitAll().anyRequest().authenticated();
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
