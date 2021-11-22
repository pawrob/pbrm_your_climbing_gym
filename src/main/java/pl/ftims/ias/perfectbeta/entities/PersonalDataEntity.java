package pl.ftims.ias.perfectbeta.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "personal_data", schema = "public")
public class PersonalDataEntity extends AbstractEntity implements Serializable {

    private String name;
    private String surname;
    private String phoneNumber;
    //m = true, f=false
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

    public PersonalDataEntity(long id, Long version, @Size(max = 30) String name, @Size(max = 30) String surname,
                              @Size(max = 15) String phoneNumber, @NotNull @Size(max = 3) String language, Boolean gender) {
        super(id, version);
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.language = language;
        this.gender = gender;
    }


}
