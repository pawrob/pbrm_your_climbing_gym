package pl.ftims.ias.your_climbing_gym.endpoints;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.ftims.ias.your_climbing_gym.auth.AuthUserDetailsService;
import pl.ftims.ias.your_climbing_gym.auth.JwtUtil;
import pl.ftims.ias.your_climbing_gym.dto.CredentialsDTO;
import pl.ftims.ias.your_climbing_gym.dto.TokenDTO;

@RestController
public class AuthenticationEndpoint {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private AuthUserDetailsService userDetailsService;
	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody CredentialsDTO credentialsDTO)
			throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					credentialsDTO.getUsername(), credentialsDTO.getPassword()));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		}
		catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
		
		UserDetails userdetails = userDetailsService.loadUserByUsername(credentialsDTO.getUsername());
		String token = jwtUtil.generateToken(userdetails);
		return ResponseEntity.ok(new TokenDTO(token));
	}
}
