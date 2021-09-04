package pl.ftims.ias.your_climbing_gym.auth.endpoints;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.ftims.ias.your_climbing_gym.auth.security.JwtRequestFilter;
import pl.ftims.ias.your_climbing_gym.auth.service.AuthUserDetailsService;
import pl.ftims.ias.your_climbing_gym.auth.service.JwtService;
import pl.ftims.ias.your_climbing_gym.dto.CredentialsDTO;
import pl.ftims.ias.your_climbing_gym.dto.TokenDTO;
import pl.ftims.ias.your_climbing_gym.exceptions.AbstractAppException;
import pl.ftims.ias.your_climbing_gym.exceptions.InvalidCredentialsException;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("auth")
@RestController
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.NEVER)
public class AuthenticationEndpoint {


    private AuthenticationManager authenticationManager;

    private AuthUserDetailsService userDetailsService;

    private JwtService jwtService;

    @Autowired
    public AuthenticationEndpoint(AuthenticationManager authenticationManager, AuthUserDetailsService userDetailsService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody CredentialsDTO credentialsDTO, HttpServletRequest request) throws AbstractAppException {
        String username = credentialsDTO.getUsername();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, credentialsDTO.getPassword()));

        } catch (DisabledException | BadCredentialsException e) {
            userDetailsService.addSessionLog(request.getRemoteAddr(), username, false);
            throw InvalidCredentialsException.createInvalidCredentialsException();
        }

        userDetailsService.addSessionLog(request.getRemoteAddr(), username, true);
        return ResponseEntity.ok(new TokenDTO(jwtService.generateToken(userDetailsService.loadUserByUsername(username))));
    }

    @GetMapping(value = "/refreshtoken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        return ResponseEntity.ok(new TokenDTO(jwtService.refreshToken(JwtRequestFilter.extractJwtFromRequest(request))));
    }


}
