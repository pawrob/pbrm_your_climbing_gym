package pl.ftims.ias.perfectbeta.mos.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.perfectbeta.entities.ClimbingGymEntity;
import pl.ftims.ias.perfectbeta.entities.RouteEntity;

import java.util.Optional;

@Repository
@Transactional(transactionManager = "mosTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY)
public interface RouteRepository extends PagingAndSortingRepository<RouteEntity, Long> {

    @Override
    Optional<RouteEntity> findById(Long id);

    Page<RouteEntity> findAllByClimbingGym(ClimbingGymEntity gym, Pageable pageable);
}
