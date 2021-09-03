package pl.ftims.ias.your_climbing_gym.dto.user_dtos;

import lombok.*;
import pl.ftims.ias.your_climbing_gym.dto.validation.Email;
import pl.ftims.ias.your_climbing_gym.dto.validation.Password;

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
