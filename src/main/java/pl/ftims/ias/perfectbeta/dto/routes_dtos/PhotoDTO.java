package pl.ftims.ias.perfectbeta.dto.routes_dtos;

import lombok.*;
import pl.ftims.ias.perfectbeta.dto.AbstractDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PhotoDTO extends AbstractDTO {

    @NotBlank
    @NotNull
    private String photoUrl;

    private Long routeId;

    public PhotoDTO(long id, Long version, String photoUrl, Long routeId) {
        super(id, version);
        this.photoUrl = photoUrl;
        this.routeId = routeId;
    }
    public PhotoDTO(long id, Long version, String photoUrl) {
        super(id, version);
        this.photoUrl = photoUrl;
    }
}
