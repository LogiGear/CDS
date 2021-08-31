package com.logigear.crm.manager.security;

import com.logigear.crm.manager.exception.JWTException;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtProvider tokenProvider;

    public JwtAuthenticationFilter(JwtProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    @SneakyThrows({ ServletException.class, IOException.class })
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        Pattern actuator_pattern = Pattern.compile("^/actuator.*");
        Pattern swagger_pattern = Pattern.compile("^[/swagger-ui].*");
        String path = request.getRequestURI();
        if (actuator_pattern.matcher(path).matches()) {

            filterChain.doFilter(request, response);
            return;
        }

        String jwt = getJwtFromRequest(request);

        try {
            if (swagger_pattern.matcher(path).matches()) {
                filterChain.doFilter(request, response);
                return;
            }
            if (tokenProvider.validateToken(jwt) && SecurityContextHolder.getContext().getAuthentication() == null) {
                String email = tokenProvider.getEmailFromToken(jwt);
                List<String> roles = tokenProvider.getRolesFromToken(jwt);
                UserDetails userDetails = User.withUsername(email).password("")
                        .roles(roles.toArray(new String[roles.size()])).build();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (JWTException ex) {
            logger.error("Invalid JWT signature");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json");
            response.getWriter().write(ex.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
