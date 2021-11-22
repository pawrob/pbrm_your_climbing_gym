package pl.ftims.ias.perfectbeta.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authentication_view", schema = "public")
public class AuthenticationViewEntity implements Serializable {

    private Long id;
    private String login;
    private String password;
    private String accessLevel;

    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    @Basic
    @Column(name = "login")
    public String getLogin() {
        return login;
    }

    @Basic
    @Column(name = "password", columnDefinition = "bpchar(60)", nullable = false, length = 60)
    public String getPassword() {
        return password;
    }

    @Basic
    @Column(name = "access_level")
    public String getAccessLevel() {
        return accessLevel;
    }


}
