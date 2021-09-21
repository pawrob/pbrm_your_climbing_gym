package pl.ftims.ias.your_climbing_gym.mos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.your_climbing_gym.entities.ClimbingGymEntity;

import java.util.Optional;

@Repository
@Transactional(transactionManager = "mosTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY)
public interface ClimbingGymRepository extends JpaRepository<ClimbingGymEntity, Long> {

    Optional<ClimbingGymEntity> findById(Long id);
}
