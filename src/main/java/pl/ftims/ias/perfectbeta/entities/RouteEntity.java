package pl.ftims.ias.perfectbeta.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "route", schema = "public")
public class RouteEntity extends AbstractEntity implements Serializable {

    private String routeName;
    private String holdsDetails;
    private String description;
    private String difficulty;
    private ClimbingGymEntity climbingGym;
    private List<PhotoEntity> photos = new ArrayList<>();


    @Basic
    @Column(name = "route_name")
    public String getRouteName() {
        return routeName;
    }


    @Basic
    @Column(name = "holds_details")
    public String getHoldsDetails() {
        return holdsDetails;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
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

    @OneToMany(mappedBy = "route", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    public List<PhotoEntity> getPhotos() {
        return photos;
    }


    public RouteEntity(String routeName, String difficulty, ClimbingGymEntity climbingGym) {
        this.routeName = routeName;
        this.difficulty = difficulty;
        this.climbingGym = climbingGym;
    }
}
