package pl.ftims.ias.your_climbing_gym.auth.endpoints;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import pl.ftims.ias.your_climbing_gym.auth.service.AuthUserDetailsService;
import pl.ftims.ias.your_climbing_gym.auth.service.JwtService;
import pl.ftims.ias.your_climbing_gym.dto.CredentialsDTO;
import pl.ftims.ias.your_climbing_gym.dto.TokenDTO;

@RequestMapping("auth")
@RestController
public class AuthenticationEndpoint {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthUserDetailsService userDetailsService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody CredentialsDTO credentialsDTO) throws Exception {
        String username = credentialsDTO.getUsername();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, credentialsDTO.getPassword()));
            //todo replace with own exception
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        return ResponseEntity.ok(new TokenDTO(jwtService.generateToken(
                userDetailsService.loadUserByUsername(username))));
    }
}
