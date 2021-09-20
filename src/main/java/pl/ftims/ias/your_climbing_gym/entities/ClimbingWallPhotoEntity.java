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
@Table(name = "climbing_wall_photo", schema = "public")
public class ClimbingWallPhotoEntity extends AbstractEntity implements Serializable {


    private ClimbingWallEntity climbingWall;
    private String filename;
    private String processingStatus;

    @Basic
    @Column(name = "filename")
    public String getFilename() {
        return filename;
    }

    @Basic
    @Column(name = "processing_status")
    public String getProcessingStatus() {
        return processingStatus;
    }

    @ManyToOne
    @JoinColumn(name = "climbing_wall_id", referencedColumnName = "id", nullable = false)
    public ClimbingWallEntity getClimbingWall() {
        return climbingWall;
    }

}
