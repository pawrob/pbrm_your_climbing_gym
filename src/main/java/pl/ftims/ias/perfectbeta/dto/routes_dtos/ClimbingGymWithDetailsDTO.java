package pl.ftims.ias.perfectbeta.dto.routes_dtos;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.ftims.ias.perfectbeta.entities.enums.GymStatusEnum;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ClimbingGymWithDetailsDTO extends ClimbingGymDTO {

    GymDetailsDTO gymDetailsDTO;

    public ClimbingGymWithDetailsDTO(long id, Long version, Long ownerId, String gymName, GymStatusEnum statusEnum, GymDetailsDTO gymDetailsDTO) {
        super(id, version, ownerId, gymName, statusEnum);
        this.gymDetailsDTO = gymDetailsDTO;
    }
}
