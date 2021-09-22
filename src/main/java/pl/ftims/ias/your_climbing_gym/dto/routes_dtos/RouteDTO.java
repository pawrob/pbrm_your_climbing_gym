package pl.ftims.ias.your_climbing_gym.dto.routes_dtos;


import lombok.*;
import pl.ftims.ias.your_climbing_gym.dto.AbstractDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RouteDTO extends AbstractDTO {

    @NotBlank
    @NotNull
    private String routeName;
    @NotBlank
    @NotNull
    private String difficulty;

    @NotNull
    private Long climbingGymId;

    public RouteDTO(long id, Long version, String routeName, String difficulty, Long climbingGymId) {
        super(id, version);
        this.routeName = routeName;
        this.difficulty = difficulty;
        this.climbingGymId = climbingGymId;
    }
}
