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
@Table(name = "climbing_wall", schema = "public")
public class ClimbingWallEntity extends AbstractEntity implements Serializable {

    private String boulderName;
    private String difficulty;
    private ClimbingGymEntity climbingGym;
    private Collection<ClimbingWallPhotoEntity> climbingWallPhotos = new ArrayList<>();


    @Basic
    @Column(name = "boulder_name")
    public String getBoulderName() {
        return boulderName;
    }

    @Basic
    @Column(name = "difficulty")
    public String getDifficulty() {
        return difficulty;
    }

    @ManyToOne
    @JoinColumn(name = "climbing_gym_id", referencedColumnName = "id", nullable = false)
    public ClimbingGymEntity getClimbingGym() {
        return climbingGym;
    }

    @OneToMany(mappedBy = "climbingWall", cascade = {CascadeType.ALL})
    public Collection<ClimbingWallPhotoEntity> getClimbingWallPhotos() {
        return climbingWallPhotos;
    }

    public ClimbingWallEntity(String boulderName, String difficulty, ClimbingGymEntity climbingGym) {
        this.boulderName = boulderName;
        this.difficulty = difficulty;
        this.climbingGym = climbingGym;
    }
}
