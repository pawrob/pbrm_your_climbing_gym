package pl.ftims.ias.perfectbeta.mok.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.ftims.ias.perfectbeta.entities.UserEntity;
import pl.ftims.ias.perfectbeta.exceptions.AbstractAppException;

public interface ManagerServiceLocal {

    Page<UserEntity> getAllUsers(Pageable page);

    UserEntity getUserById(Long id) throws AbstractAppException;

    UserEntity createManagerAccountWithAccessLevel(UserEntity userEntity);

    UserEntity deactivateManager(Long id) throws AbstractAppException;

    UserEntity activateManager(Long id) throws AbstractAppException;
}
