package pl.ftims.ias.your_climbing_gym.auth.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.your_climbing_gym.entities.AuthenticationViewEntity;

import java.util.List;

@Repository
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY)
public interface AuthViewRepository extends CrudRepository<AuthenticationViewEntity, Long> {

    List<AuthenticationViewEntity> findByLogin(String login);
}
