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
@Table(name = "route", schema = "public")
public class RouteEntity extends AbstractEntity implements Serializable {

    private String routeName;
    private String difficulty;
    private ClimbingGymEntity climbingGym;
    private Collection<RoutePhotoEntity> routePhotos = new ArrayList<>();


    @Basic
    @Column(name = "route_name")
    public String getRouteName() {
        return routeName;
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

    @OneToMany(mappedBy = "route", cascade = {CascadeType.ALL})
    public Collection<RoutePhotoEntity> getRoutePhotos() {
        return routePhotos;
    }

    public RouteEntity(String routeName, String difficulty, ClimbingGymEntity climbingGym) {
        this.routeName = routeName;
        this.difficulty = difficulty;
        this.climbingGym = climbingGym;
    }
}
