package pl.ftims.ias.your_climbing_gym.mok.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.ftims.ias.your_climbing_gym.entities.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity,Long> {
}
