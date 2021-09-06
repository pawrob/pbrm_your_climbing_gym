package pl.ftims.ias.your_climbing_gym.entities;

import lombok.*;
import javax.persistence.*;

@Data
@MappedSuperclass
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractEntity {

    private Long id;
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    @Basic
    @Version
    @Column(name = "version", nullable = false)
    public Long getVersion() {
        return version;
    }



    public void setVersion(Long version) {
        this.version = version;
    }
}
