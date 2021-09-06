package pl.ftims.ias.your_climbing_gym.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.your_climbing_gym.entities.SessionLogEntity;


@Repository
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY)
public interface SessionLogRepository extends JpaRepository<SessionLogEntity, Long> {
}
