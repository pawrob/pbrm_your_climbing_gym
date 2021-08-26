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
@DiscriminatorValue("MANAGER")
@PrimaryKeyJoinColumn(name = "access_level_id")
@Table(name = "manager", schema = "public")
public class ManagerEntity extends AccessLevelEntity implements Serializable {

    public ManagerEntity(boolean isActive, UserEntity userEntity) {
        super(isActive, userEntity);
    }
}
