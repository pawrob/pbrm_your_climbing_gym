package pl.ftims.ias.your_climbing_gym.Entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Entity
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user", schema = "public")
public class UserEntity implements Serializable {
    private Long id;
    private String login;
    private String password;
    private String email;
    private Boolean isActive;
    private Boolean isVerified;
    private String passwordResetToken;
    private OffsetDateTime passwordResetTokenTimestamp;
    private String emailResetToken;
    private OffsetDateTime emailResetTokenTimestamp;
    private Short failedLogin;
    private Long version;
    private PersonalDataEntity personalData;

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
    @Column(name = "password",columnDefinition = "bpchar(60)", nullable = false, length = 60)
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
    @Column(name = "password_reset_token",columnDefinition = "bpchar(60)", nullable = false, length = 64)
    public String getPasswordResetToken() {
        return passwordResetToken;
    }
    @Basic
    @Column(name = "password_reset_token_timestamp")
    public OffsetDateTime getPasswordResetTokenTimestamp() {
        return passwordResetTokenTimestamp;
    }

    @Basic
    @Column(name = "email_reset_token",columnDefinition = "bpchar(60)", nullable = false, length = 64)
    public String getEmailResetToken() {
        return emailResetToken;
    }

    @Basic
    @Column(name = "email_reset_token_timestamp")
    public OffsetDateTime getEmailResetTokenTimestamp() {
        return emailResetTokenTimestamp;
    }

    @Basic
    @Column(name = "failed_login")
    public Short getFailedLogin() {
        return failedLogin;
    }

    @Basic
    @Column(name = "version")
    public Long getVersion() {
        return version;
    }


    @OneToOne(mappedBy = "user", cascade = {CascadeType.ALL})
    public PersonalDataEntity getPersonalData() {
        return personalData;
    }


    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }
}
