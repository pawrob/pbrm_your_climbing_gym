package pl.ftims.ias.your_climbing_gym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.ftims.ias.your_climbing_gym.dto.validation.Password;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
public class ChangePasswordDTO {

    @Getter
    @Setter
    @NotNull
    @Password
    private String newPassword;

    @Getter
    @Setter
    @NotBlank
    private String oldPassword;

    public ChangePasswordDTO() {
    }
}
