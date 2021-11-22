package pl.ftims.ias.perfectbeta.mos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.perfectbeta.entities.GymMaintainerEntity;
import pl.ftims.ias.perfectbeta.entities.UserEntity;

import java.util.List;

@Repository
@Transactional(transactionManager = "mosTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY)
public interface GymMaintainerRepository extends JpaRepository<GymMaintainerEntity, Long> {

    List<GymMaintainerEntity> findByUser(UserEntity user);
}
