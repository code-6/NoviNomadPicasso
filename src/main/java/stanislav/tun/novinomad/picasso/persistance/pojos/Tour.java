package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.fasterxml.jackson.annotation.JsonRootName;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Set;

@Indexed
@Entity(name = "tours")
@JsonRootName(value = "tour")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private Calendar startDate;
    @Column
    private Calendar endDate;
    @Column
    private int days;
    @Column
    private String tittle;
    @Column
    private String description;

    @OneToMany(mappedBy = "tour")
    private Set<Driver> drivers;

    public Tour() {
        if(startDate != null && endDate != null)
            days = endDate.get(Calendar.DAY_OF_YEAR) - startDate.get(Calendar.DAY_OF_YEAR);
    }

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

    public void addDriver(Driver driver){
        drivers.add(driver);
    }

    public void addDriver(long id, String firstName, String middleName, String lastName){
        var driver = new Driver();
        driver.setId(id);
        driver.setFirstName(firstName);
        driver.setMiddleName(middleName);
        driver.setLastName(lastName);
        drivers.add(driver);
    }
}
