package pl.ftims.ias.your_climbing_gym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.ftims.ias.your_climbing_gym.dto.validation.Password;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDTO {
    @NotNull
    @NotBlank
    @Password
    private String password;
}
