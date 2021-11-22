package pl.ftims.ias.perfectbeta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.ftims.ias.perfectbeta.dto.validation.Password;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
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
