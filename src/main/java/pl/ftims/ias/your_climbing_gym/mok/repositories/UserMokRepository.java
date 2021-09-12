package pl.ftims.ias.your_climbing_gym.mok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ftims.ias.your_climbing_gym.entities.UserEntity;

import java.util.Optional;

@Repository
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY)
public interface UserMokRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findByLogin(String login);
    Optional<UserEntity> findByEmail(String email);
}
