package com.logigear.crm.authenticate.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.logigear.crm.authenticate.model.User;
import com.logigear.crm.authenticate.property.AppProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

@Component
public class JwtProvider {

	private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

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

	public Date getExpirationDateFromToken(String token) {
		return getAllClaimsFromToken(token).getExpiration();
	}

	public Object getEmailFromToken(String token) {
		return getAllClaimsFromToken(token).get("email");
	}

	public Object getRolesFromToken(String token) {
		return getAllClaimsFromToken(token).get("roles");
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String generateToken(User user) {

		final Date createdDate = new Date();
		final Date expirationDate = new Date(createdDate.getTime() + properties.getJwt().getExpirationMillis() * 1000);

		ArrayList<String> roles = new ArrayList<String>();
		user.getRoles().forEach(role -> roles.add(Objects.toString(role.getName(), "")));

		return Jwts.builder().setSubject(Long.toString(user.getId())).setIssuedAt(createdDate)
				.setExpiration(expirationDate).claim("email", user.getEmail()).claim("roles",roles)
				.signWith(SignatureAlgorithm.HS512, properties.getJwt().getSecret())
				.compact();
	}

	public Boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(properties.getJwt().getSecret()).parseClaimsJws(token);
			return !isTokenExpired(token);
		} catch (SignatureException ex) {
			logger.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			logger.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			logger.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			logger.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			logger.error("JWT claims string is empty.");
		}
		return false;
	}
}
