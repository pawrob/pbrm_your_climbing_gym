package pl.ftims.ias.your_climbing_gym.mok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.your_climbing_gym.entities.PersonalDataEntity;
import pl.ftims.ias.your_climbing_gym.entities.UserEntity;

import java.util.Optional;

@Repository
@Transactional(transactionManager = "mokTransactionManager", isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY)
public interface PersonalDataMokRepository extends JpaRepository<PersonalDataEntity, Long> {


    Optional<PersonalDataEntity> findByUser(UserEntity userEntity);

    PersonalDataEntity findByIdAndVersion(long id, Long version);
}