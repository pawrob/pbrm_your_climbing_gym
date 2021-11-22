package pl.ftims.ias.perfectbeta.dto.routes_dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.ftims.ias.perfectbeta.dto.AbstractDTO;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class GymMaintainerDTO extends AbstractDTO {

    @NotNull
    public Long maintainerId;
    @NotNull
    public Long gymId;
    @NotNull
    public Boolean isActive;

    public GymMaintainerDTO(long id, Long version, @NotNull Long maintainerId, @NotNull Long gymId, @NotNull Boolean isActive) {
        super(id, version);
        this.maintainerId = maintainerId;
        this.gymId = gymId;
        this.isActive = isActive;
    }
}
