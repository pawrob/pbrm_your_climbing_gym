package pl.ftims.ias.your_climbing_gym.entities;

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
@Table(name = "personal_data", schema = "public")
public class PersonalDataEntity implements Serializable {
    private Long userId;
    private String name;
    private String surname;
    private String phoneNumber;
    private Boolean gender;
    private String language = "PL";
    private Long version;
    private UserEntity user;

    @Id
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Basic
    @Column(name = "surname")
    public String getSurname() {
        return surname;
    }

    @Basic
    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Basic
    @Column(name = "gender")
    public Boolean getGender() {
        return gender;
    }

    @Basic
    @Column(name = "language")
    public String getLanguage() {
        return language;
    }

    @Basic
    @Version
    @Column(name = "version")
    public Long getVersion() {
        return version;
    }

    @OneToOne(optional = false)
    @PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
    public UserEntity getUser() {
        return user;
    }

    public PersonalDataEntity(long userId, String name, String surname, String phoneNumber, String language, Boolean gender, long version) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.language = language;
        this.gender = gender;
        this.version = version;
    }
}
