package pl.ftims.ias.your_climbing_gym.dto;

import lombok.Getter;
import pl.ftims.ias.your_climbing_gym.dto.validation.Email;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class EmailDTO {

    @Email
    @NotBlank
    @NotNull
    private String email;
}
