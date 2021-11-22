package pl.ftims.ias.perfectbeta.dto.user_dtos;

import lombok.*;
import pl.ftims.ias.perfectbeta.dto.validation.Email;
import pl.ftims.ias.perfectbeta.dto.validation.Password;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RegistrationDTO {

    @NotNull
    @NotBlank
    private String login;

    @Email
    @NotBlank
    @NotNull
    private String email;

    @Password
    @NotBlank
    @NotNull
    private String password;
}
