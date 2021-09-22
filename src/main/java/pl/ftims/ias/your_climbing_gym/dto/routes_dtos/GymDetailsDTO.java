package pl.ftims.ias.your_climbing_gym.dto.routes_dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.ftims.ias.your_climbing_gym.dto.AbstractDTO;

import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class GymDetailsDTO extends AbstractDTO {

    @Size(max = 64)
    private String country;
    @Size(max = 64)
    private String city;
    @Size(max = 64)
    private String street;
    @Size(max = 64)
    private String number;
    @Size(max = 2048)
    private String description;

    public GymDetailsDTO(long id, Long version, @Size(max = 64) String country, @Size(max = 64) String city,
                         @Size(max = 64) String street, @Size(max = 64) String number, @Size(max = 2048) String description) {
        super(id, version);
        this.country = country;
        this.city = city;
        this.street = street;
        this.number = number;
        this.description = description;
    }
}
