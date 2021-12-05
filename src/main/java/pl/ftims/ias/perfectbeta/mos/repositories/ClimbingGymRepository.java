package pl.ftims.ias.perfectbeta.mos.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.perfectbeta.entities.ClimbingGymEntity;
import pl.ftims.ias.perfectbeta.entities.UserEntity;

import java.util.Optional;

@Repository
@Transactional(transactionManager = "mosTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY)
public interface ClimbingGymRepository extends PagingAndSortingRepository<ClimbingGymEntity, Long> {

    @Override
    Page<ClimbingGymEntity> findAll(Pageable pageable);

    Optional<ClimbingGymEntity> findById(Long id);

    Page<ClimbingGymEntity> findByOwner(UserEntity owner, Pageable pageable);

    @Query("SELECT g FROM ClimbingGymEntity g where g.status='VERIFIED'")
    Page<ClimbingGymEntity> findAllVerified(Pageable pageable);

    @Query("SELECT g FROM ClimbingGymEntity g where g.status='VERIFIED' and g.id=:id")
    Optional<ClimbingGymEntity> findVerifiedById(@Param("id") Long id);
}
