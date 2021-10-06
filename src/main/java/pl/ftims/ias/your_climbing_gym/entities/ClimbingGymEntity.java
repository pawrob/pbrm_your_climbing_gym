package pl.ftims.ias.your_climbing_gym.entities;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import pl.ftims.ias.your_climbing_gym.entities.enums.GymStatusEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "climbing_gym", schema = "public")
public class ClimbingGymEntity extends AbstractEntity implements Serializable {

    private String gymName;
    private GymStatusEnum status;
    private List<RouteEntity> routes = new ArrayList<>();
    private List<GymMaintainerEntity> maintainers = new ArrayList<>();
    private UserEntity owner;
    private GymDetailsEntity gymDetails;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public UserEntity getOwner() {
        return owner;
    }

    @Basic
    @Column(name = "gym_name")
    public String getGymName() {
        return gymName;
    }

    @Basic
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public GymStatusEnum getStatus() {
        return status;
    }

    @OneToMany(mappedBy = "climbingGym", cascade = {CascadeType.ALL})
    public List<RouteEntity> getRoutes() {
        return routes;
    }

    @OneToMany(mappedBy = "maintainedGym", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    public List<GymMaintainerEntity> getMaintainers() {
        return maintainers;
    }

    @OneToOne(mappedBy = "gym", cascade = {CascadeType.ALL})
    public GymDetailsEntity getGymDetails() {
        return gymDetails;
    }

    public ClimbingGymEntity(String gymName, UserEntity owner) {
        this.gymName = gymName;
        this.owner = owner;
        this.status = GymStatusEnum.UNVERIFIED;
    }


}
