package pl.ftims.ias.your_climbing_gym.dto.access_levels;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ManagerDTO extends AccessLevelDTO {
    public ManagerDTO(long id, Long version, String accessLevel, Boolean isActive) {
        super(id, version, accessLevel, isActive);
    }
}
