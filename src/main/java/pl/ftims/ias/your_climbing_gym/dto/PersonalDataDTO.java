package pl.ftims.ias.your_climbing_gym.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.ftims.ias.your_climbing_gym.dto.validation.PhoneNumber;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PersonalDataDTO extends AbstractDTO {

    @Size(max = 30)
    private String name;

    @Size(max = 30)
    private String surname;

    @Size(max = 15)
    @PhoneNumber
    private String phoneNumber;

    @NotNull
    @Size(max = 3)
    private String language;

    private Boolean gender;

    public PersonalDataDTO(long id, Long version, @Size(max = 30) String name, @Size(max = 30) String surname, @Size(max = 15) String phoneNumber, @NotNull @Size(max = 3) String language, Boolean gender) {
        super(id, version);
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.language = language;
        this.gender = gender;
    }
}
