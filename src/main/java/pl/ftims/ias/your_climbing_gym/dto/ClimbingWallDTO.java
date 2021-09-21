package pl.ftims.ias.your_climbing_gym.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClimbingWallDTO extends AbstractDTO {

    @NotBlank
    @NotNull
    private String boulderName;
    @NotBlank
    @NotNull
    private String difficulty;

    @NotNull
    private Long climbingGymId;

    public ClimbingWallDTO(long id, Long version, String boulderName, String difficulty, Long climbingGymId) {
        super(id, version);
        this.boulderName = boulderName;
        this.difficulty = difficulty;
        this.climbingGymId = climbingGymId;
    }
}
