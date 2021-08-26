package pl.ftims.ias.your_climbing_gym.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.ftims.ias.your_climbing_gym.entities.AuthenticationViewEntity;
import pl.ftims.ias.your_climbing_gym.auth.repositories.AuthViewRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthUserDetailsService implements UserDetailsService {


    @Autowired
    AuthViewRepository authViewRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<AuthenticationViewEntity> credentials = authViewRepository.findByLogin(username);
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        for (AuthenticationViewEntity credential : credentials) {
            if (credential.getAccessLevel().equals("ADMINISTRATOR")) {
                roles.add(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"));
            }
            if (credential.getAccessLevel().equals("CLIMBER")) {
                roles.add(new SimpleGrantedAuthority("ROLE_CLIMBER"));
            }
            if (credential.getAccessLevel().equals("MANAGER")) {
                roles.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
            }
        }
        //todo return not found exception
        return new User(credentials.get(0).getLogin(), credentials.get(0).getPassword(), roles);
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
