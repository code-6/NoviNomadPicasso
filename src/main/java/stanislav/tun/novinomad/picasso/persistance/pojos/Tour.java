package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonRootName;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Entity(name = "tours")
@JsonRootName(value = "tour")
@EntityListeners(AuditingEntityListener.class)
public class Tour extends AbstractEntity implements Serializable {
//    @JsonIgnore
//    @Transient
//    private static final String datePattern = "dd-mm-yyyy";
//
//    @JsonIgnore
//    @Transient
//    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    //@Temporal(TemporalType.DATE)
    private LocalDateTime startDate;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    //@Temporal(TemporalType.DATE)
    private LocalDateTime endDate;

    @Column
    private int days;
    @Column
    @NotNull
    private String tittle;
    @Column
    @JsonIgnore
    private String description;

    //@Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JsonManagedReference
    @Fetch(FetchMode.JOIN)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="tours_drivers", joinColumns = @JoinColumn(name = "tour_id"), inverseJoinColumns = @JoinColumn(name="driver_id"))
    private Set<Driver> drivers = new HashSet<>();

    @JsonManagedReference
    @Fetch(FetchMode.JOIN)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="tours_guides", joinColumns = @JoinColumn(name = "tour_id"), inverseJoinColumns = @JoinColumn(name="guide_id"))
    private Set<Guide> guides = new HashSet<>();


    @Fetch(FetchMode.JOIN)
    @OneToMany( mappedBy = "tour", orphanRemoval = true)
    @JsonIgnore
    Set<DriverTourIntervals> driverIntervals = new HashSet<DriverTourIntervals>();


    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    @OneToMany( mappedBy = "tour", orphanRemoval = true)
    Set<GuideTourIntervals> guideIntervals = new HashSet<>();

    public void addDriver(Optional<Driver> driver){
        drivers.add(driver.get());
    }

    public void addGuide(Optional<Guide> guide){
        guides.add(guide.get());
    }

    public void addDriver(String firstName, String middleName, String lastName){
        var driver = new Driver();
        driver.setFirstName(firstName);
        driver.setMiddleName(middleName);
        driver.setLastName(lastName);
        drivers.add(driver);
    }

    public void addGuide(String firstName, String middleName, String lastName){
        var guide = new Guide();
        guide.setFirstName(firstName);
        guide.setMiddleName(middleName);
        guide.setLastName(lastName);
        guides.add(guide);
    }

    public void addDriver(Set<Driver> _drivers){
        if(_drivers != null)
            drivers.addAll(_drivers);
    }

    public void addGuide(Set<Guide> _guides){
        if(_guides != null)
            guides.addAll(_guides);
    }

    public void deleteDriver(Driver driver){
        drivers.remove(driver);
    }

    public void deleteDriver(Collection<Driver> _drivers){
        drivers.removeAll(_drivers);
    }

    public void deleteGuide(Guide guide){
        guides.remove(guide);
    }

    public void deleteGuide(Collection<Guide> _guides){
        guides.removeAll(_guides);
    }

    public int getDays() {
        return days;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    @JsonIgnore
    @Transient
    public String getStartDateForPicker(){
        return "\""+startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+"\"";
    }

    @JsonIgnore
    @Transient
    public String getEndDateForPicker(){
        return "\""+endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+"\"";
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        if(endDate != null)
            days = endDate.getDayOfYear() - this.startDate.getDayOfYear()+1;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        if(startDate != null)
            days = this.endDate.getDayOfYear() - startDate.getDayOfYear()+1;
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

    public Set<DriverTourIntervals> getDriverIntervals() {
        return driverIntervals;
    }

    public void setDriverIntervals(Set<DriverTourIntervals> driverIntervals) {
        this.driverIntervals = driverIntervals;
    }

    public Set<Guide> getGuides() {
        return guides;
    }

    public void setGuides(Set<Guide> guides) {
        this.guides = guides;
    }

    public Set<GuideTourIntervals> getGuideIntervals() {
        return guideIntervals;
    }

    public void setGuideIntervals(Set<GuideTourIntervals> guideIntervals) {
        this.guideIntervals = guideIntervals;
    }
}
