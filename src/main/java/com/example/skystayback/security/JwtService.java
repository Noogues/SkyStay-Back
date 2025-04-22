package com.example.skystayback.security;

import com.example.skystayback.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class JwtService {

    private static final String SECRETkEY = "375ec1bfd98afb0e55aa10907716db40941a160687ba7dd09bdc3ec9a39206dd";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUserRole(String token) {
        return extractClaim(token, claims -> claims.get("rol", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails, String userCode, String rol) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userCode", userCode);
        extraClaims.put("rol", rol);
        return generateToken(extraClaims, userDetails);
    }

    public String generateToken(
            Map<String, Object> extaClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extaClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 ))
                .signWith(getSignInkey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }


    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInkey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    private Key getSignInkey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRETkEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public long getRemainingTime(String token) {
        Date expirationDate = extractExpiration(token);
        long remainingTime = expirationDate.getTime() - System.currentTimeMillis();
        return remainingTime > 0 ? remainingTime : 0;
    }
}
