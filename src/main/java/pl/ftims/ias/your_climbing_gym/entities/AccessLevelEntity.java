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
@Table(name = "access_level_table", schema = "public")
public class AccessLevelEntity extends AbstractEntity implements Serializable {

    private String accessLevel;
    private Boolean isActive;
    private UserEntity user;


    @Basic
    @Column(name = "access_level")
    public String getAccessLevel() {
        return accessLevel;
    }

    @Basic
    @Column(name = "is_active")
    public Boolean getActive() {
        return isActive;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public UserEntity getUser() {
        return user;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public AccessLevelEntity(boolean isActive, UserEntity userEntity, String accessLevel) {
        this.setActive(isActive);
        this.setUser(userEntity);
        this.setAccessLevel(accessLevel);
    }

}
