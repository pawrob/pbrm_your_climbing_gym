package pl.ftims.ias.your_climbing_gym.exceptions;


import lombok.NoArgsConstructor;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
@NoArgsConstructor
public class GymNotFoundException extends AbstractAppException {

    public static final String GYM_WITH_ID_NOT_EXISTS = "Gym with identifier: %s does not exist";
    public static final String GYM_WITH_OWNER_NOT_EXISTS = "Gym with owner: %s does not exist";

    private GymNotFoundException(String message) {
        super(message);
    }

    public static GymNotFoundException createGymWithProvidedIdNotFoundException(long id) {
        return new GymNotFoundException(String.format(GYM_WITH_ID_NOT_EXISTS, id));
    }

    public static GymNotFoundException createGymWithProvidedOwnerNotFoundException(String ownerLogin) {
        return new GymNotFoundException(String.format(GYM_WITH_OWNER_NOT_EXISTS, ownerLogin));
    }
}
