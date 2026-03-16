package com.beautywellness.beauty_wellness.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//clasa utilitara pentru generarea si validarea token-urilor JWT
@Component
public class JwtService {

    //cheia secreta injectata din application.properties
    @Value("${jwt.secret}")
    private String secretKey;

    //timpul de expirare injectat din application.properties
    @Value("${jwt.expiration}")
    private long expirationTime;

    //extrage emailul (username) din token
    public String extractUsername(String token) {
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token-ul nu poate fi null sau gol");
        }
        return extractClaim(token, Claims::getSubject);
    }

    //genereaza un token JWT pentru un utilizator
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    //genereaza token cu claims extra
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //verifica daca token-ul este valid pentru utilizatorul dat
    public boolean isTokenValid(String token, UserDetails userDetails) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        final String username = extractUsername(token);
        return (username != null && username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    //verifica daca token-ul a expirat
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //extrage data de expirare din token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //extrage un claim specific din token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //extrage toate claims-urile din token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //returneaza cheia de semnare
    private Key getSigningKey() {
        byte[] keyBytes = hexStringToByteArray(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //converteste string hex in bytes
    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}