package com.exams.config.jwt;

import io.jsonwebtoken.*;
import lombok.extern.java.Log;
import org.springframework.security.web.header.Header;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
@Log
public class JwtProvide {
    public String generateToken(String login) {
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(Date.from(ZonedDateTime.now().plusDays(5).toInstant()))
                .signWith(SignatureAlgorithm.HS512, "goOdSOft")
                .compact();
    }

    public boolean validateToken(String token) throws ExpiredJwtException {
        try {
            Jwts.parser().setSigningKey("goOdSOft").parseClaimsJws(token);
            return true;

        } catch (ExpiredJwtException expEx) {
            log.severe("exp jwt");
        } catch (UnsupportedJwtException unsEx) {
            log.severe("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            log.severe("Malformed jwt");
        } catch (SignatureException sEx) {
            log.severe("Invalid signature");
        }
        return false;
    }

    public boolean expiredToken(String token) {
        try {
            Jwts.parser().setSigningKey("goOdSOft").parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.severe("Token expired");
        }
        return false;
    }

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey("goOdSOft").parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
