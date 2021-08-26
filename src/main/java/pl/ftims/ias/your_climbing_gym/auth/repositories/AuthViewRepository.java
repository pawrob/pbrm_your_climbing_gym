package pl.ftims.ias.your_climbing_gym.auth.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.ftims.ias.your_climbing_gym.entities.AuthenticationViewEntity;

import java.util.List;

@Repository
public interface AuthViewRepository extends CrudRepository<AuthenticationViewEntity,Long> {

    List<AuthenticationViewEntity> findByLogin(String login);
}
