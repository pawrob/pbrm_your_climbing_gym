package pl.ftims.ias.your_climbing_gym.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import javax.persistence.*;

@Data
@MappedSuperclass
@EqualsAndHashCode
public abstract class AbstractEntity {
    @Setter(AccessLevel.PROTECTED)
    private long id;
    private Long version = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    @Basic
    @Version
    @Column(name = "version", nullable = false)
    public Long getVersion() {
        return version;
    }
}
