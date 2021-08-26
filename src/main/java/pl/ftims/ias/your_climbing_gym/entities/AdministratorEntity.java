package pl.ftims.ias.your_climbing_gym.entities;


import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DiscriminatorValue("ADMINISTRATOR")
@PrimaryKeyJoinColumn(name = "access_level_id")
@Table(name = "administrator", schema = "public")
public class AdministratorEntity extends AccessLevelEntity implements Serializable {

    public AdministratorEntity(boolean isActive, UserEntity userEntity) {
        super(isActive, userEntity);
    }
}
