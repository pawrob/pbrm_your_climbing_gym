package pl.ftims.ias.your_climbing_gym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.ftims.ias.your_climbing_gym.dto.validation.Email;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {


    @Email
    @NotBlank
    @NotNull
    private String email;
}
