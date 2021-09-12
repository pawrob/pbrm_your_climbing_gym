package pl.ftims.ias.your_climbing_gym.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.your_climbing_gym.entities.AuthenticationViewEntity;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(transactionManager = "authTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY)
public interface AuthViewRepository extends JpaRepository<AuthenticationViewEntity, Long> {

    Optional<List<AuthenticationViewEntity>> findByLogin(String login);
}
