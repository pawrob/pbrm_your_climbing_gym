package pl.ftims.ias.your_climbing_gym.entities;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "climbing_gym", schema = "public")
public class ClimbingGymEntity extends AbstractEntity implements Serializable {

    private String gymName;
    private Collection<ClimbingWallEntity> climbingWalls = new ArrayList<>();

    @Basic
    @Column(name = "gym_name")
    public String getGymName() {
        return gymName;
    }

    @OneToMany(mappedBy = "climbingGym", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    public Collection<ClimbingWallEntity> getClimbingWalls() {
        return climbingWalls;
    }
}
