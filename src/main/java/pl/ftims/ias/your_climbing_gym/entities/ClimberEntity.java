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
@DiscriminatorValue("CLIMBER")
@PrimaryKeyJoinColumn(name = "access_level_id")
@Table(name = "climber", schema = "public")
public class ClimberEntity extends AccessLevelEntity implements Serializable {

}
