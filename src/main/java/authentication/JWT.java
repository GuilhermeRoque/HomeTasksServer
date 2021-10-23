package authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

public class JWT {
    final static Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String buildToken(String subject){
        return Jwts.builder()
                .setIssuer("localhost:8080")
                .setSubject(subject)
                .setIssuedAt(new Date())
                .signWith(secretKey)
                .compact();

    }

    public static String getSubject(String bearerToken){
        String token = bearerToken.substring("Bearer".length()).trim();
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }

}