package pl.ftims.ias.perfectbeta.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.perfectbeta.entities.SessionLogEntity;


@Repository
@Transactional(transactionManager = "authTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY)
public interface SessionLogRepository extends JpaRepository<SessionLogEntity, Long> {
}
