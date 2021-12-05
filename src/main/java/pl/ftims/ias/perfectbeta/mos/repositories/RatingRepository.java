package pl.ftims.ias.perfectbeta.mos.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.perfectbeta.entities.RatingEntity;

import java.util.Optional;

@Repository
@Transactional(transactionManager = "mosTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY)
public interface RatingRepository extends PagingAndSortingRepository<RatingEntity, Long> {
    @Override
    Optional<RatingEntity> findById(Long id);
}

