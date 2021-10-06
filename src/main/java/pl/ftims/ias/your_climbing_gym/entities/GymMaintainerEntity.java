package pl.ftims.ias.your_climbing_gym.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gym_maintainer", schema = "public")
public class GymMaintainerEntity extends AbstractEntity implements Serializable {
    ClimbingGymEntity maintainedGym;
    UserEntity user;
    Boolean isActive;

    @Basic
    @Column(name = "is_active")
    public Boolean getActive() {
        return isActive;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public UserEntity getUser() {
        return user;
    }

    @ManyToOne
    @JoinColumn(name = "climbing_gym_id", referencedColumnName = "id", nullable = false)
    public ClimbingGymEntity getMaintainedGym() {
        return maintainedGym;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}

