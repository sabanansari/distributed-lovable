package com.ansari.distributed_lovable.common_lib.security;

import com.ansari.distributed_lovable.common_lib.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

@Component
public class AuthUtil {

    @Value("${jwt.secret-key}")
   private String jwtSecretKey;
    //private String jwtSecretKey = "gfhgfdhdffhgfhgfhgfhgfhgfherterterhgfb345345thggfdghgfdghgfdgfdbdffgbdfgt45345";

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UserDto user) {
        // Logic to generate JWT access token based on user details
        return Jwts.builder()
                .subject(user.username())
                .claim("userId", user.id().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*100))
                .signWith(getSecretKey())
                .compact();

    }

    public JwtUserPrincipal verifyAccessToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Long userId = Long.parseLong(claims.get("userId").toString());
        String username = claims.getSubject();

        return new JwtUserPrincipal(userId, username,null, new ArrayList<>());
    }

    public long getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !(authentication.getPrincipal() instanceof JwtUserPrincipal)) {
            throw new AuthenticationCredentialsNotFoundException("No JWT found");
        }
        JwtUserPrincipal principal = (JwtUserPrincipal) authentication.getPrincipal();
        return principal.userId();
    }
}


