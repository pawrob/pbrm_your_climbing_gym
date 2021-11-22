package pl.ftims.ias.perfectbeta.mok.services;

import pl.ftims.ias.perfectbeta.entities.UserEntity;
import pl.ftims.ias.perfectbeta.exceptions.AbstractAppException;

public interface ManagerServiceLocal {

    UserEntity createManagerAccountWithAccessLevel(UserEntity userEntity);

    UserEntity deactivateManager(Long id) throws AbstractAppException;

    UserEntity activateManager(Long id) throws AbstractAppException;
}
