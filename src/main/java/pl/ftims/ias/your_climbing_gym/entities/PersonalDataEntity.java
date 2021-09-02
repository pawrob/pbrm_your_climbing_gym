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
public class PersonalDataEntity extends AbstractEntity implements Serializable {

    private String name;
    private String surname;
    private String phoneNumber;
    private Boolean gender;
    private String language = "PL";
    private UserEntity user;

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

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public UserEntity getUser() {
        return user;
    }

    public PersonalDataEntity(UserEntity user) {
        this.user = user;
    }

    public PersonalDataEntity(String name, String surname, String phoneNumber, String language, Boolean gender) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.language = language;
        this.gender = gender;
    }
}
