package pl.ftims.ias.perfectbeta.auth.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.ftims.ias.perfectbeta.exceptions.UserNotFoundAppException;

import java.util.List;
import java.util.Map;

public interface JwtServiceLocal {


    public String generateToken(UserDetails userDetails);

    public String refreshToken(String authToken);

    public String doGenerateRefreshToken(Map<String, Object> claims, String subject);

    public boolean validateToken(String authToken) throws UserNotFoundAppException;

    public String getUsernameFromToken(String token);

    public List<SimpleGrantedAuthority> getRolesFromToken(String token);

}
