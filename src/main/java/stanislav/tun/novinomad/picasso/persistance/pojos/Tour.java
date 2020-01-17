package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonRootName;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Entity(name = "tours")
@JsonRootName(value = "tour")
public class Tour extends AbstractEntity implements Serializable {
    @JsonIgnore
    @Transient
    private static final String datePattern = "yyyy-MM-dd'T'HH:mm";

    @JsonIgnore
    @Transient
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

    @Column
    @DateTimeFormat(pattern = datePattern)
    private LocalDateTime startDate;

    @Column
    @DateTimeFormat(pattern = datePattern)
    private LocalDateTime endDate;

    @Column
    private int days;
    @Column
    private String tittle;
    @Column
    @JsonIgnore
    private String description;

    //@Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="tours_drivers", joinColumns = {@JoinColumn(name = "tour_id")}, inverseJoinColumns = {@JoinColumn(name="driver_id")})
    private Set<Driver> drivers = new HashSet<>();

    public void addDriver(Optional<Driver> driver){
        drivers.add(driver.get());
    }

    public void addDriver(String firstName, String middleName, String lastName){
        var driver = new Driver();
        driver.setFirstName(firstName);
        driver.setMiddleName(middleName);
        driver.setLastName(lastName);
        drivers.add(driver);
    }

    public void addDriver(Set<Driver> _drivers){
        drivers.addAll(_drivers);
    }

    public void deleteDriver(Driver driver){
        drivers.remove(driver);
    }

    public void deleteDriver(Collection<Driver> _drivers){
        drivers.removeAll(_drivers);
    }

    public int getDays() {
        return days;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(Set<Driver> drivers) {
        this.drivers = drivers;
    }
}
