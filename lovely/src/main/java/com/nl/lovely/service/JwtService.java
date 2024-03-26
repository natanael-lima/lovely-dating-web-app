package com.nl.lovely.service;

import java.util.Date;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public interface JwtService {
	public String getToken(UserDetails user);
	public String getUsernameFromToken(String token);
	public boolean isTokenValid(String token, UserDetails userDetails);
	//public Date getExpiration(String token);
	//public boolean isTokenExpired(String token);
	//public Claims getAllClaims(String token);
	//public <T> T getClaim(String token, Function<Claims,T> claimsResolver);
	 
}
