package pl.ftims.ias.your_climbing_gym.dto;

import lombok.*;

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

    public ClimbingGymDTO(long id, Long version, Long ownerId, String gymName) {
        super(id, version);
        this.ownerId = ownerId;
        this.gymName = gymName;
    }
}
