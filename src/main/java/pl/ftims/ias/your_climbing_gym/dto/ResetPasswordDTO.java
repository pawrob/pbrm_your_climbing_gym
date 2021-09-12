package pl.ftims.ias.your_climbing_gym.dto;

import lombok.Getter;
import lombok.Setter;
import pl.ftims.ias.your_climbing_gym.dto.validation.Password;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ResetPasswordDTO {
    @Getter
    @Setter
    @NotNull
    @Password
    private String newPassword;

    @Getter
    @Setter
    @NotBlank
    private String newPasswordConfirmation;
}
