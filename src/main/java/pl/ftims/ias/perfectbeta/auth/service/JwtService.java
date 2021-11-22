package pl.ftims.ias.perfectbeta.auth.service;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.perfectbeta.auth.repositories.AuthViewRepository;
import pl.ftims.ias.perfectbeta.exceptions.UserNotFoundAppException;

import java.util.*;

@Service
@Transactional(transactionManager = "authTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
public class JwtService implements JwtServiceLocal {

    @Autowired
    private AuthViewRepository authViewRepository;
    private String secret;
    private int jwtExpirationInMs;
    private int jwtRefreshExpirationInMs;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Value("${jwt.expirationDateInMs}")
    public void setJwtExpirationInMs(int jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    @Value("${jwt.refreshExpirationInMs}")
    public void setJwtRefreshExpirationInMs(int jwtRefreshExpirationInMs) {
        this.jwtRefreshExpirationInMs = jwtRefreshExpirationInMs;
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

    public String refreshToken(String authToken) {
        Map<String, Object> claims = new HashMap<>();
        Collection<? extends GrantedAuthority> roles = getRolesFromToken(authToken);

        if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"))) claims.put("isAdmin", true);
        if (roles.contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) claims.put("isManager", true);
        if (roles.contains(new SimpleGrantedAuthority("ROLE_CLIMBER"))) claims.put("isClimber", true);

        return doGenerateRefreshToken(claims, getUsernameFromToken(authToken));
    }

    public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public boolean validateToken(String authToken) throws UserNotFoundAppException {
        try {

            if (authViewRepository.findByLogin(getUsernameFromToken(authToken)).get().isEmpty())
                return false;

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

        if (isAdmin != null && isAdmin) roles.add(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"));
        if (isManager != null && isManager) roles.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
        if (isClimber != null && isClimber) roles.add(new SimpleGrantedAuthority("ROLE_CLIMBER"));

        return roles;
    }

}
