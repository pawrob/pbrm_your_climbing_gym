package pl.ftims.ias.your_climbing_gym.auth;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JwtUtil {

    private String secret;
    private int jwtExpirationInMs;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Value("${jwt.expirationDateInMs}")
    public void setJwtExpirationInMs(int jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

        if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"))) claims.put("isAdmin", true);
        if (roles.contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) claims.put("isManager", true);
        if (roles.contains(new SimpleGrantedAuthority("ROLE_CLIMBER"))) claims.put("isClimber", true);

        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, secret).compact();

    }

    public boolean validateToken(String authToken) {
        try {
            //todo potrzebne te claimsy?
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
        } catch (ExpiredJwtException ex) {
            throw ex;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.getSubject();

    }

    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

        List<SimpleGrantedAuthority> roles = new ArrayList<>();

        Boolean isAdmin = claims.get("isAdmin", Boolean.class);
        Boolean isManager = claims.get("isManager", Boolean.class);
        Boolean isClimber = claims.get("isClimber", Boolean.class);

        if (isAdmin != null && isAdmin)roles.add(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"));
        if (isManager != null && isManager) roles.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
        if (isClimber != null && isClimber) roles.add(new SimpleGrantedAuthority("ROLE_CLIMBER"));

        return roles;
    }

}