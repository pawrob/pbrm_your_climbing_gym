package pl.ftims.ias.perfectbeta.dto.routes_dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.ftims.ias.perfectbeta.entities.enums.GymStatusEnum;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ClimbingGymWithMaintainersDTO extends ClimbingGymDTO {

    public List<GymMaintainerDTO> maintainerDTO;

    public ClimbingGymWithMaintainersDTO(long id, Long version, Long ownerId, String gymName, GymStatusEnum statusEnum, List<GymMaintainerDTO> maintainerDTO) {
        super(id, version, ownerId, gymName, statusEnum);
        this.maintainerDTO = maintainerDTO;
    }
}
