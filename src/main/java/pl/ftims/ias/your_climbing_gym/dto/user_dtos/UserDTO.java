package pl.ftims.ias.your_climbing_gym.dto.user_dtos;


import lombok.*;
import pl.ftims.ias.your_climbing_gym.dto.AbstractDTO;
import pl.ftims.ias.your_climbing_gym.dto.validation.Email;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends AbstractDTO {

    @NotNull
    @NotBlank
    private String login;

    @Email
    @NotBlank
    @NotNull
    private String email;


    private Boolean isActive;
    private Boolean isVerified;


    public UserDTO(long id, Long version, String login, String email, Boolean isActive, Boolean isVerified) {
        super(id, version);
        this.login = login;
        this.email = email;
        this.isActive = isActive;
        this.isVerified = isVerified;
    }
}
