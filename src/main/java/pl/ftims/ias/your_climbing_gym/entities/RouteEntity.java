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
@Table(name = "route", schema = "public")
public class RouteEntity extends AbstractEntity implements Serializable {

    private String routeName;
    private String photoWithBoxesLink;
    private String photoWithNumbersLink;
    private String holdsDetails;
    private String difficulty;
    private ClimbingGymEntity climbingGym;


    @Basic
    @Column(name = "route_name")
    public String getRouteName() {
        return routeName;
    }

    @Basic
    @Column(name = "photo_with_boxes_link")
    public String getPhotoWithBoxesLink() {
        return photoWithBoxesLink;
    }

    @Basic
    @Column(name = "photo_with_numbers_link")
    public String getPhotoWithNumbersLink() {
        return photoWithNumbersLink;
    }

    @Basic
    @Column(name = "holds_details")
    public String getHoldsDetails() {
        return holdsDetails;
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


    public RouteEntity(String routeName, String difficulty, ClimbingGymEntity climbingGym) {
        this.routeName = routeName;
        this.difficulty = difficulty;
        this.climbingGym = climbingGym;
    }
}
