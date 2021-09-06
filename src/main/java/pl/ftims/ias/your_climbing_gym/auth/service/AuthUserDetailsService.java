package pl.ftims.ias.your_climbing_gym.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.your_climbing_gym.auth.repositories.AuthViewRepository;
import pl.ftims.ias.your_climbing_gym.auth.repositories.SessionLogRepository;
import pl.ftims.ias.your_climbing_gym.auth.repositories.UserAuthRepository;
import pl.ftims.ias.your_climbing_gym.entities.AuthenticationViewEntity;
import pl.ftims.ias.your_climbing_gym.entities.SessionLogEntity;
import pl.ftims.ias.your_climbing_gym.entities.UserEntity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
public class AuthUserDetailsService implements UserDetailsService {


    AuthViewRepository authViewRepository;
    SessionLogRepository sessionLogRepository;
    UserAuthRepository userRepository;

    @Autowired
    public AuthUserDetailsService(AuthViewRepository authViewRepository, SessionLogRepository sessionLogRepository, UserAuthRepository userRepository) {
        this.authViewRepository = authViewRepository;
        this.userRepository = userRepository;
        this.sessionLogRepository = sessionLogRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        List<AuthenticationViewEntity> credentials = new ArrayList<>();
        List<SimpleGrantedAuthority> roles = new ArrayList<>();

        Optional<List<AuthenticationViewEntity>> authenticationViewEntityList = authViewRepository.findByLogin(username);
        if (authenticationViewEntityList.get().size() != 0) {
            credentials = authenticationViewEntityList.get();
        } else {
            throw new UsernameNotFoundException(username);
        }


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
        return new User(credentials.get(0).getLogin(), credentials.get(0).getPassword(), roles);
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

    public void addSessionLog(String ip, String username, Boolean isSuccessful) {
        Optional<UserEntity> user = userRepository.findByLogin(username);

        if (user.isPresent()) {
            userRepository.save(checkIfNeedBlock(user.get(), isSuccessful));
            sessionLogRepository.save(new SessionLogEntity(OffsetDateTime.now(), ip, isSuccessful, user.get()));
        } else {
            sessionLogRepository.save(new SessionLogEntity(OffsetDateTime.now(), ip, isSuccessful, null));
        }


    }

    public UserEntity checkIfNeedBlock(UserEntity userEntity, Boolean isSuccessful) {
        if (Boolean.FALSE.equals(isSuccessful)) {
            userEntity.setFailedLogin(userEntity.getFailedLogin() + 1);
        }
        if (Boolean.TRUE.equals(userEntity.getActive()) && userEntity.getFailedLogin() != 0 && Boolean.TRUE.equals(isSuccessful)) {
            userEntity.setFailedLogin(0);
        }

        if (userEntity.getFailedLogin() > 3) {
            userEntity.setActive(false);
        }

        return userEntity;
    }
}
