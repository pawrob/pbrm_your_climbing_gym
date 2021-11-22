package pl.ftims.ias.perfectbeta.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
