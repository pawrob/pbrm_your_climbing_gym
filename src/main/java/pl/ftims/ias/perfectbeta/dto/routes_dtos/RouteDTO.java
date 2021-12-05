package pl.ftims.ias.perfectbeta.dto.routes_dtos;


import lombok.*;
import pl.ftims.ias.perfectbeta.dto.AbstractDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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
    private String description;
    @NotBlank
    @NotNull
    private String holdsDetails;
    @NotBlank
    @NotNull
    private Double avgRating;
    @NotNull
    private Long climbingGymId;

    public List<PhotoDTO> photos = new ArrayList<>();


    public RouteDTO(long id, Long version, String routeName, String difficulty, String description, String holdsDetails, Double avgRating, Long climbingGymId, List<PhotoDTO> photos) {
        super(id, version);
        this.routeName = routeName;
        this.difficulty = difficulty;
        this.description = description;
        this.holdsDetails = holdsDetails;
        this.avgRating = avgRating;
        this.climbingGymId = climbingGymId;
        this.photos = photos;
    }

    public RouteDTO(String routeName, String difficulty, String description, String holdsDetails, Long climbingGymId) {
        this.routeName = routeName;
        this.difficulty = difficulty;
        this.description = description;
        this.holdsDetails = holdsDetails;
        this.climbingGymId = climbingGymId;
    }
}
