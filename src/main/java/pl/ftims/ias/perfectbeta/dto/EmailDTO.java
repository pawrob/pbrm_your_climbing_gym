package pl.ftims.ias.perfectbeta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.ftims.ias.perfectbeta.dto.validation.Email;

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
