package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity(name = "drivers")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
//@JsonRootName(value = "driver")
@EntityListeners(AuditingEntityListener.class)
public class Driver extends AbstractEntity implements Serializable {

    @Column
    //@JsonIgnore
    protected String lastName;

    @Column
    //@JsonIgnore
    protected String firstName;

    @Column
    //@JsonIgnore
    protected String middleName;

    protected String fullName;

    @Column
    private String car;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "drivers")
    //@JsonBackReference
    @Fetch(FetchMode.JOIN)
    //@Cascade(org.hibernate.annotations.CascadeType.ALL)
    protected Set<Tour> tours = new HashSet<>();

    @OneToMany(mappedBy = "driver", orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    private Set<DriverTourIntervals> driverTourIntervals = new HashSet<DriverTourIntervals>();

    public Driver() {

    }

    public Driver(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
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

    public Set<DriverTourIntervals> getDriverTourIntervals() {
        return driverTourIntervals;
    }

    public void setDriverTourIntervals(Set<DriverTourIntervals> driverTourIntervals) {
        this.driverTourIntervals = driverTourIntervals;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Driver driver = (Driver) obj;
        return id == driver.id && (firstName == driver.firstName || (firstName != null && firstName.equals(driver.getFirstName()))) && (lastName == driver.lastName || (lastName != null && lastName.equals(driver.getLastName())));
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fullName);
    }

    @Override
    public String toString() {
        return "Driver{" +
                "\n     id=" + id +
                ",\n    fullName='" + getFullName() + '\'' +
                ",\n    car='" + car + '\'' +
                ",\n    createdBy='" + createdBy + '\'' +
                ",\n    creationDate=" + creationDate +
                ",\n    lastModifiedBy='" + lastModifiedBy + '\'' +
                ",\n    lasModifyDate=" + lasModifyDate +
                ",\n    deleted=" + deleted +
                "\n}";
    }

    public String toString(DateTimeRange i) {

        try{
            return "Driver {" +
                    "\n         id=" + id +
                    ",\n        fullName='" + getFullName() + '\'' +
                    ",\n        car='" + car + '\'' +
                    ",\n        interval=" + i.toString() +
                    "\n}";
        }catch (NullPointerException e){
            return "Driver {" +
                    "\n         id=" + id +
                    ",\n        fullName='" + getFullName() + '\'' +
                    ",\n        car='" + car + '\'' +
                    "\n}";
        }

    }
}
