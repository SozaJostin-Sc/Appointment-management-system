// JwtService.java
package com.gestionMedica.main.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class JwtService {

    //Llave secreta tomada de application.dev.properties
    @Value("${jwt.secret:mySecretKey12345678901234567890123456789012}")
    private String secretKey;

    /// Tiempo de expiracion
    @Value("${jwt.expiration:86400000}") // 24 horas por defecto
    private long jwtExpiration;

    @Value("${jwt.refres.token.expiration:604800000}")
    private long jwtRefreshTokenExpiration;

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //GENERACIÓN DEL TOKEN
    public String generateToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails){
        return buildToken(new HashMap<>(), userDetails, jwtRefreshTokenExpiration);
    }

    /// Construccion del token
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /// Extraer el username del token.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /// Metodo que se encarga de verificar si el token es valido.
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /// Verifica si el token expiro.
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /// Extraccion del tiempo de expiracion.
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /// Extraccion de roles
    public List<String> extractRoles(String token){
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}