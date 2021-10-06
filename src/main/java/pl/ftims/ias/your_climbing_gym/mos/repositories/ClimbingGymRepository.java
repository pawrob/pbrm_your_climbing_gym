package pl.ftims.ias.your_climbing_gym.mos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.your_climbing_gym.entities.ClimbingGymEntity;
import pl.ftims.ias.your_climbing_gym.entities.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(transactionManager = "mosTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY)
public interface ClimbingGymRepository extends JpaRepository<ClimbingGymEntity, Long> {

    Optional<ClimbingGymEntity> findById(Long id);

    Optional<List<ClimbingGymEntity>> findByOwner(UserEntity owner);

    @Query("SELECT g FROM ClimbingGymEntity g where g.status='VERIFIED'")
    List<ClimbingGymEntity> findAllVerified();

    @Query("SELECT g FROM ClimbingGymEntity g where g.status='VERIFIED' and g.id=:id")
    Optional<ClimbingGymEntity> findVerifiedById(@Param("id") Long id);
}
