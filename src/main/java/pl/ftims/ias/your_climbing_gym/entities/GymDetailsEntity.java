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
@Table(name = "climbing_gym_details", schema = "public")
public class GymDetailsEntity extends AbstractEntity implements Serializable {

    private ClimbingGymEntity gym;
    private String country;
    private String city;
    private String street;
    private String number;
    private String description;

    @Basic
    @Column(name = "country")
    public String getCountry() {
        return country;
    }

    @Basic
    @Column(name = "city")
    public String getCity() {
        return city;
    }

    @Basic
    @Column(name = "street")
    public String getStreet() {
        return street;
    }

    @Basic
    @Column(name = "number")
    public String getNumber() {
        return number;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    @OneToOne
    @JoinColumn(name = "climbing_gym_id", referencedColumnName = "id", nullable = false)
    public ClimbingGymEntity getGym() {
        return gym;
    }

    public GymDetailsEntity(ClimbingGymEntity gym) {
        this.gym = gym;
    }
}
