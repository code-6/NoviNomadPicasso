package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Indexed
@Entity(name = "tours")
@JsonRootName(value = "tour")
public class Tour {
    @Id
    @Column(name = "tour_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Calendar startDate;
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Calendar endDate;
    @Column
    private int days;
    @Column
    private String tittle;
    @Column
    private String description;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(name="tours_drivers", joinColumns = {@JoinColumn(name = "tour_id")}, inverseJoinColumns = {@JoinColumn(name="driver_id")})
    private Set<Driver> drivers = new HashSet<>();

    public Tour() {
        if(startDate != null && endDate != null)
            days = endDate.get(Calendar.DAY_OF_YEAR) - startDate.get(Calendar.DAY_OF_YEAR);
    }

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

    @Autowired
    @JsonIgnore
    @Transient
    ObjectMapper mapper;

    public int getDays() {
        return days;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
