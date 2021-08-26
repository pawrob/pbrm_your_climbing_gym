package pl.ftims.ias.your_climbing_gym.entities;

import lombok.*;

import javax.persistence.*;

@Data
@MappedSuperclass
@EqualsAndHashCode
public abstract class AbstractEntity {
    @Setter(AccessLevel.PROTECTED)
    private long id;
    private Long version;

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
