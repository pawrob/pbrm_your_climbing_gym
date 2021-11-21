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
    @NotBlank
    @NotNull
    private String photoWithBoxesLink;
    @NotBlank
    @NotNull
    private String photoWithNumbersLink;
    @NotBlank
    @NotNull
    private String holdsDetails;

    @NotNull
    private Long climbingGymId;


    public RouteDTO(long id, Long version, String routeName, String difficulty, String photoWithBoxesLink, String photoWithNumbersLink, String holdsDetails, Long climbingGymId) {
        super(id, version);
        this.routeName = routeName;
        this.difficulty = difficulty;
        this.photoWithBoxesLink = photoWithBoxesLink;
        this.photoWithNumbersLink = photoWithNumbersLink;
        this.holdsDetails = holdsDetails;
        this.climbingGymId = climbingGymId;
    }
}
