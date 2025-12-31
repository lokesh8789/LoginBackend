package com.login.webflux.security;

import com.login.webflux.utils.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    public String generateToken(String username, Map<String, ?> claims) {
        return createToken(Optional.ofNullable(claims).orElse(Collections.emptyMap()), username);
    }

    private String createToken(Map<String, ?> claims, String username) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + Constants.ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .signWith(getSignKey(), Jwts.SIG.HS512)
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] bytes = Decoders.BASE64.decode(Constants.SIGNING_KEY);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, String username) {
        return Optional.ofNullable(token)
                .filter(t -> !isTokenExpired(t))
                .map(this::extractUsername)
                .map(un -> un.equals(username))
                .orElse(false);
    }

    public Optional<String> getJwtToken(ServerHttpRequest request) {
        return Optional.ofNullable(request.getHeaders().getFirst(Constants.AUTHORIZATION))
                .filter(header -> header.startsWith(Constants.TOKEN_PREFIX))
                .map(header -> header.substring(Constants.TOKEN_PREFIX.length()));
    }
}
