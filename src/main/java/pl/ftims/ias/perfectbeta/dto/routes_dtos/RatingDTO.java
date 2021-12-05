package pl.ftims.ias.perfectbeta.dto.routes_dtos;

import lombok.*;
import pl.ftims.ias.perfectbeta.dto.AbstractDTO;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RatingDTO extends AbstractDTO {


    private String comment;
    @NotNull
    private Double rate;
    private Long routeId;
    private Long userId;

}