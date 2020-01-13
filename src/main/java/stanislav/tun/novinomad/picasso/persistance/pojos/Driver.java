package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.Cascade;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Indexed
@Entity(name = "drivers")
@JsonRootName(value = "driver")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "driverId")
public class Driver implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    private long driverId;

    @Column
    private String lastName;

    @Column
    private String firstName;

    @Column
    private String middleName;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "drivers")
    @JsonBackReference
    //@Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Tour> tours = new HashSet<>();

    public Driver() {

    }

    public Driver(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Set<Tour> getTours() {
        return tours;
    }

    public void setTours(Set<Tour> tours) {
        this.tours = tours;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        if(middleName == null)
            return "";

        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFullName() {
        return String.format("%s %s %s", firstName == null ? "" : firstName, middleName == null ? "" : middleName, lastName == null ? "" : lastName);
    }


}
