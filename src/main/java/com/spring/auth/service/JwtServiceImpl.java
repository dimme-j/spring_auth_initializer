package com.spring.auth.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.spring.auth.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl {
	@Value("${security.jwt.secret-key}")
    private String secretKey;
	
	@Value("${security.jwt.expiration-time}")
	private Integer exptime;
	
	private SecretKey getSecretKey(){
		byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
	    return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String generateAccessToken(User user) {
		return Jwts.builder()
				.setSubject(user.getPassword())
				.claim("userid",user.getUserid().toString())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + exptime))
				.signWith(getSecretKey())
				.compact();
	}

	public Boolean isExpired(String jwtToken) {
		try {
			String jwt = extractToken(jwtToken);
			if (jwt != null) {
				Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwt)
						.getBody();
				return false;
			}else {
				return true;
			}
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			return true;
		}
	}
	public String extractToken(String authHeader) {
	    if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        return authHeader.substring(7); // remove "Bearer "
	    }
	    return null;
	}
}
