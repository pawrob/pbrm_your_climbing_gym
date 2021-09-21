package pl.ftims.ias.your_climbing_gym.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClimbingGymDTO extends AbstractDTO {
    public Long ownerId;
    public String gymName;

    public ClimbingGymDTO(long id, Long version, Long ownerId, String gymName) {
        super(id, version);
        this.ownerId = ownerId;
        this.gymName = gymName;
    }
}
