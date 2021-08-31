package com.logigear.crm.employees.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import com.logigear.crm.employees.config.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.logigear.crm.employees.exception.JWTException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtProvider {

    private AppProperties properties;

    @Autowired
    public JwtProvider(AppProperties properties) {
        this.properties = properties;
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(properties.getJwt().getSecret()).parseClaimsJws(token).getBody();
    }

    public Long getUserIdFromToken(String token) {
        return Long.parseLong(getAllClaimsFromToken(token).getSubject());
    }

    public String getEmailFromToken(String token) {
        return Objects.toString(getAllClaimsFromToken(token).get("email"), "");
    }

    public String getPasswordFromToken(String token) {
        return Objects.toString(getAllClaimsFromToken(token).get("password"), "");
    }

    public ArrayList<String> getRolesFromToken(String token) {
        return (ArrayList<String>) getAllClaimsFromToken(token).get("roles");
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(properties.getJwt().getSecret()).parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (SignatureException ex) {
            throw new JWTException("Invalid JWT signature", ex);
        } catch (MalformedJwtException ex) {
            throw new JWTException("Invalid JWT token", ex);
        } catch (ExpiredJwtException ex) {
            throw new JWTException("Expired JWT token", ex);
        } catch (UnsupportedJwtException ex) {
            throw new JWTException("Unsupported JWT token", ex);
        } catch (IllegalArgumentException ex) {
            throw new JWTException("JWT claims is empty.", ex);
        }
    }
}
