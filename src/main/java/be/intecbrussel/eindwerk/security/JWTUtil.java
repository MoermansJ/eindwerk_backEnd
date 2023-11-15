package be.intecbrussel.eindwerk.security;

import be.intecbrussel.eindwerk.model.User;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JWTUtil {
    //Thank-you Bogdan for helping me set up Spring Security! :)
    private final String secretKey = "mysupersecretkeymysupersecretkeymysupersecretkeymysupersecretkeymysupersecretkeymysupersecretkey";
    private final long accessTokenValidity = 60; //1 hour
    private final JwtParser jwtParser;

    //TOKEN_HEADER and TOKEN_PREFIX are constants used for working with the Authorization header in HTTP requests.
    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public JWTUtil() {
        this.jwtParser = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))).build();
    }

    //PROPERTIES: sub = username; exp = expiration date;
    public String createToken(User user, String role) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());

        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS512)
                .compact();
    }

    // Resolves the JWT claims from the HttpServletRequest.
    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException expiredJwtException) {
            req.setAttribute("expired ", expiredJwtException.getMessage());
            throw expiredJwtException;
        } catch (Exception e) {
            req.setAttribute("invalid ", e.getMessage());
            throw e;
        }
    }


    //Extracts the JWT token from the Authorization header in the HTTP request.
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return bearerToken;
    }


    //Converts String token to Claims
    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }


    //Validates expiration date
    public boolean validateClaims(Claims claims) throws AuthenticationException {
        return claims.getExpiration().after(new Date());
    }

    public boolean validateClaims(String token) {
        return validateClaims(this.parseJwtClaims(token));
    }
}
