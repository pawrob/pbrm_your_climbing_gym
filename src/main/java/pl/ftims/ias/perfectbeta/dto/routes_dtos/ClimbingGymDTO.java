package pl.ftims.ias.perfectbeta.dto.routes_dtos;

import lombok.*;
import pl.ftims.ias.perfectbeta.dto.AbstractDTO;
import pl.ftims.ias.perfectbeta.entities.enums.GymStatusEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClimbingGymDTO extends AbstractDTO {


    @NotNull
    public Long ownerId;
    @NotBlank
    @NotNull
    public String gymName;
    @NotBlank
    @NotNull
    public GymStatusEnum status;

    public ClimbingGymDTO(long id, Long version, Long ownerId, String gymName, GymStatusEnum statusEnum) {
        super(id, version);
        this.ownerId = ownerId;
        this.gymName = gymName;
        this.status = statusEnum;
    }
}
