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
    private final long accessTokenValidity = 60 * 60 * 1000; //1 hour
    private final JwtParser jwtParser;

    //TOKEN_HEADER and TOKEN_PREFIX are constants used for working with the Authorization header in HTTP requests.
    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public JWTUtil() {
        this.jwtParser = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))).build();
    }

    //    createToken:
//    Generates a JWT for a given AuthUser object.
//    Uses the subject of the JWT to be the email of the user.
//    Sets the expiration time based on the current time and the configured validity period.
//    Signs the JWT using the HMAC SHA-512 algorithm.
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

    // Parse not the JWT parse the JWS claims from a given token using the initialized JwtParser.
    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    // Resolves the JWT claims from the HttpServletRequest.
    // It calls resolveToken to get the token from the request and then parses the claims using parseJwtClaims.
    // If the token is expired or any other exception occurs, it's caught and propagated.
    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException expiredJwtException) {
            req.setAttribute("expired", expiredJwtException.getMessage());
            throw expiredJwtException;
        } catch (Exception expiredJwtException) {
            req.setAttribute("invalid", expiredJwtException.getMessage());
            throw expiredJwtException;
        }
    }

    // Extracts the JWT token from the Authorization header in the HTTP request.
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    //Validates if the expiration time of the claims is after the current time.
    //If the claims are expired, it throws an ExpiredJwtException.
    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean validateClaims(String token) {
        //throws exceptions because the JWT token String becomes malformed, should contain 2 period-characters "."
        //TO DO: FIX !!
        try {
            System.out.println(token);
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            System.out.println(claims.getBody());
            return validateClaims(claims.getBody());
        } catch (Exception e) {
            throw e;
        }
    }

}
