package pl.ftims.ias.perfectbeta.entities;

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
@Table(name = "rating", schema = "public")
public class RatingEntity extends AbstractEntity implements Serializable {


    private Double rate;
    private String comment = "";
    private RouteEntity route;
    private UserEntity user;

    @Basic
    @Column(name = "rate")
    public Double getRate() {
        return rate;
    }

    @Basic
    @Column(name = "comment")
    public String getComment() {
        return comment;
    }

    @ManyToOne
    @JoinColumn(name = "route_id", referencedColumnName = "id", nullable = false)
    public RouteEntity getRoute() {
        return route;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public UserEntity getUser() {
        return user;
    }
}
