package com.cafe.cafemanagementsystem.JWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {

    // define a secret key
    // our json web-token is generated based on this secret key
    private String secret = "top_secret";

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    // extract username from token
    public String extractUsernameFromToken(String token) {
        return (extractClaims(token, Claims::getSubject));
    }

    // extract expiration time of token
    public Date extractExpirationOfToken(String token) {
        return (extractClaims(token, Claims::getExpiration));
    }

    // checking if token provided by user is expired or not
    public boolean isTokenExpired(String token) {
        return extractExpirationOfToken(token).before(new Date());
    }

    // validating a token
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsernameFromToken(token);
        if (username.equals(userDetails.getUsername()) && !isTokenExpired(token)) {
            return true;
        }
        return false;
    }

    // creating a token
    public String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // token expires in 10 hrs
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // generating a token
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }

}
