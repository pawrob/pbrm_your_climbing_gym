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
@Table(name = "route_photo", schema = "public")
public class RoutePhotoEntity extends AbstractEntity implements Serializable {


    private RouteEntity route;
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
    @JoinColumn(name = "route_id", referencedColumnName = "id", nullable = false)
    public RouteEntity getRoute() {
        return route;
    }

}
