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
    private String username;
    @NotNull
    private Double rate;
    private Long routeId;
    private Long userId;

    public RatingDTO(long id, Long version, String comment, Double rate, Long routeId, Long userId,String username) {
        super(id, version);
        this.comment = comment;
        this.rate = rate;
        this.routeId = routeId;
        this.userId = userId;
        this.username = username;
    }
}
