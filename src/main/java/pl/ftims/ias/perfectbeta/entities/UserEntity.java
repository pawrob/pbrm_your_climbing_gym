package pl.ftims.ias.perfectbeta.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user", schema = "public")
public class UserEntity extends AbstractEntity implements Serializable {

    private String login;
    private String password;
    private String email;
    private Boolean isActive = true;
    private Boolean isVerified = false;
    private String passwordResetToken;
    private OffsetDateTime passwordResetTokenTimestamp;
    private String emailResetToken;
    private OffsetDateTime emailResetTokenTimestamp;
    private String verifyToken;
    private OffsetDateTime verifyTokenTimestamp;
    private Integer failedLogin = 0;
    private Collection<AccessLevelEntity> accessLevels = new ArrayList<>();
    private PersonalDataEntity personalData;


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
    @Column(name = "email")
    public String getEmail() {
        return email;
    }


    @Basic
    @Column(name = "is_active")
    public Boolean getActive() {
        return isActive;
    }

    @Basic
    @Column(name = "is_verified")
    public Boolean getVerified() {
        return isVerified;
    }

    @Basic
    @Column(name = "password_reset_token", columnDefinition = "bpchar(60)", length = 64)
    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    @Basic
    @Column(name = "password_reset_token_timestamp")
    public OffsetDateTime getPasswordResetTokenTimestamp() {
        return passwordResetTokenTimestamp;
    }

    @Basic
    @Column(name = "email_reset_token", columnDefinition = "bpchar(60)", length = 64)
    public String getEmailResetToken() {
        return emailResetToken;
    }

    @Basic
    @Column(name = "email_reset_token_timestamp")
    public OffsetDateTime getEmailResetTokenTimestamp() {
        return emailResetTokenTimestamp;
    }

    @Basic
    @Column(name = "verify_token", columnDefinition = "bpchar(60)", length = 64)
    public String getVerifyToken() {
        return verifyToken;
    }

    @Basic
    @Column(name = "verify_token_timestamp")
    public OffsetDateTime getVerifyTokenTimestamp() {
        return verifyTokenTimestamp;
    }

    @Basic
    @Column(name = "failed_login")
    public Integer getFailedLogin() {
        return failedLogin;
    }


    @OneToOne(mappedBy = "user", cascade = {CascadeType.ALL})
    public PersonalDataEntity getPersonalData() {
        return personalData;
    }

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    public Collection<AccessLevelEntity> getAccessLevels() {
        return accessLevels;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public UserEntity(String login, String email, String password) {
        this.login = login;
        this.password = password;
        this.email = email;
    }
}
