package pl.ftims.ias.perfectbeta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.ftims.ias.perfectbeta.dto.validation.Password;

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
