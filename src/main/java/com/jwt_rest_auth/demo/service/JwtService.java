package com.jwt_rest_auth.demo.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jwt_rest_auth.demo.entity.AppUser;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time-ms}")
    private String expirationTimeMs;

    @Value("${security.jwt.issuer}")
    private String issuer;

    public String createJwtToken(AppUser user) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        var key = Keys.hmacShaKeyFor(keyBytes);
        var now = System.currentTimeMillis();

        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date(now))
                .issuer(issuer)
                .expiration(new Date(now + 6000000))
                .signWith(key)
                .compact();
    }

    public Claims getTokenClaims(String token) {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        var key = Keys.hmacShaKeyFor(keyBytes);

        try {
            var claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Date expirationDate = claims.getExpiration();
            Date currentDate = new Date();

            if (currentDate.before(expirationDate)) {
                return claims;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
}
