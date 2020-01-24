package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity(name = "drivers")
@JsonRootName(value = "driver")
public class Driver extends AbstractEntity implements Serializable {

    @Column
    @JsonIgnore
    private String lastName;

    @Column
    @JsonIgnore
    private String firstName;

    @Column
    @JsonIgnore
    private String middleName;

    private String fullName;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "drivers")
    @JsonBackReference
    @Fetch(FetchMode.JOIN)
    //@Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Tour> tours = new HashSet<>();

    @OneToMany( mappedBy = "driver", orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    Set<DriverTourIntervals> intervals = new HashSet<DriverTourIntervals>();

    public Driver() {

    }

    public Driver(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Set<Tour> getTours() {
        return tours;
    }

    public void setTours(Set<Tour> tours) {
        this.tours = tours;
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
        if (middleName == null)
            return "";

        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFullName() {
        return String.format("%s %s %s", firstName == null ? "" : firstName, middleName == null ? "" : middleName, lastName == null ? "" : lastName);
    }

    public Set<DriverTourIntervals> getIntervals() {
        return intervals;
    }

    public void setIntervals(Set<DriverTourIntervals> intervals) {
        this.intervals = intervals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Driver)) return false;
        if (!super.equals(o)) return false;
        Driver driver = (Driver) o;
        return fullName.equals(driver.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fullName);
    }
}
