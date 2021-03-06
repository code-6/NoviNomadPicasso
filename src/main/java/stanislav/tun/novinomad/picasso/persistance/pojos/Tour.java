package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonRootName;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import stanislav.tun.novinomad.picasso.controllers.TourController;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.ValidationException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Entity(name = "tours")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
//@JsonRootName(value = "tour")
@EntityListeners(AuditingEntityListener.class)
public class Tour extends AbstractEntity implements Serializable {
//    //@JsonIgnore
//    @Transient
//    private static final String datePattern = "dd-mm-yyyy";
//
//    //@JsonIgnore
//    @Transient
//    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

    ////@JsonIgnore
    @Transient
    Logger logger = LoggerFactory.getLogger(Tour.class);

    ////@JsonIgnore
    @Transient
    private static DateTimeFormatter f = new DateTimeFormatterBuilder()
            .appendPattern("dd.MM.yyyy")
            .optionalStart()
            .appendPattern(" HH:mm")
            .optionalEnd()
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .toFormatter();

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    //@Temporal(TemporalType.DATE)
    private LocalDateTime startDate;

    @Column
    private String fileName;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    //@Temporal(TemporalType.DATE)
    private LocalDateTime endDate;

    @Column
    private int days;

    @Column
    private int touristsCount;

    @Column
    @NotNull
    private String tittle;
    @Column
    //@JsonIgnore
    private String description;

    //@Cascade(org.hibernate.annotations.CascadeType.ALL)
    //@JsonManagedReference
    @Fetch(FetchMode.JOIN)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tours_drivers", joinColumns = @JoinColumn(name = "tour_id"), inverseJoinColumns = @JoinColumn(name = "driver_id"))
    private Set<Driver> drivers = new HashSet<>();

    //@JsonManagedReference
    @Fetch(FetchMode.JOIN)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tours_guides", joinColumns = @JoinColumn(name = "tour_id"), inverseJoinColumns = @JoinColumn(name = "guide_id"))
    private Set<Guide> guides = new HashSet<>();


    //@JsonManagedReference
    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "tour", orphanRemoval = true)
    //@JsonIgnore
    Set<DriverTourIntervals> driverIntervals = new HashSet<DriverTourIntervals>();

    ////@JsonIgnore
    //@JsonManagedReference
    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "tour", orphanRemoval = true)
    Set<GuideTourIntervals> guideIntervals = new HashSet<>();

    public void addDriver(Optional<Driver> driver) {
        drivers.add(driver.get());
    }

    public void addGuide(Optional<Guide> guide) {
        guides.add(guide.get());
    }

    public void addDriver(String firstName, String middleName, String lastName) {
        var driver = new Driver();
        driver.setFirstName(firstName);
        driver.setMiddleName(middleName);
        driver.setLastName(lastName);
        drivers.add(driver);
    }

    public void addGuide(String firstName, String middleName, String lastName) {
        var guide = new Guide();
        guide.setFirstName(firstName);
        guide.setMiddleName(middleName);
        guide.setLastName(lastName);
        guides.add(guide);
    }

    public void addDriver(Set<Driver> _drivers) {
        if (_drivers != null)
            drivers.addAll(_drivers);
    }

    public void addGuide(Set<Guide> _guides) {
        if (_guides != null)
            guides.addAll(_guides);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void deleteDriver(Driver driver) {
        drivers.remove(driver);
    }

    public void deleteDriver(Collection<Driver> _drivers) {
        drivers.removeAll(_drivers);
    }

    public void deleteGuide(Guide guide) {
        guides.remove(guide);
    }

    public void deleteGuide(Collection<Guide> _guides) {
        guides.removeAll(_guides);
    }

    public int getDays() {
        return days;
    }

    public LocalDateTime getStartDate() {

        return startDate;
    }

    public boolean hasEmptyDays() {

        return false;
    }

    ////@JsonIgnore
    @Transient
    public LocalDateTime getMinDriverStart() {

        LocalDateTime min = null;

        var i = driverIntervals.iterator();
        if (i.hasNext()) {
            min = i.next().getStartDate();
        }
        while (i.hasNext()) {
            var next = i.next();
            if (next.getStartDate().isBefore(min)) {
                min = next.getStartDate();
            }
        }


        return min;
    }

    ////@JsonIgnore
    @Transient
    public LocalDateTime getMaxDriverEnd() {

        LocalDateTime max = null;
        var i = driverIntervals.iterator();
        if (i.hasNext()) {
            max = i.next().getStartDate();
        }
        while (i.hasNext()) {
            var next = i.next();
            if (next.getStartDate().isAfter(max)) {
                max = next.getStartDate();
            }
        }

        return max;
    }

    ////@JsonIgnore
    @Transient
    public LocalDateTime getMinGuideStart() {

        LocalDateTime min = null;
        var i = guideIntervals.iterator();
        if (i.hasNext()) {
            min = i.next().getStartDate();
        }
        while (i.hasNext()) {
            var next = i.next();
            if (next.getStartDate().isBefore(min)) {
                min = next.getStartDate();
            }
        }
        logger.debug("Minimal start date of guide = " + f.format(min));
        return min;
    }

    ////@JsonIgnore
    @Transient
    public LocalDateTime getMaxGuideEnd() {

        LocalDateTime max = null;
        var i = guideIntervals.iterator();
        if (i.hasNext()) {
            max = i.next().getStartDate();
        }
        while (i.hasNext()) {
            var next = i.next();
            if (next.getStartDate().isAfter(max)) {
                max = next.getStartDate();
            }
        }
        logger.debug("Maximal end date of guide = " + f.format(max));
        return max;
    }

    public String getStartDateFormatted() {
        return f.format(startDate);
    }

    public String getEndDateFormatted() {
        return f.format(endDate);
    }

    //@JsonIgnore
    @Transient
    public String getStartDateForPicker() {
        return "\"" + startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\"";
    }

    //@JsonIgnore
    @Transient
    public String getEndDateForPicker() {
        return "\"" + endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\"";
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        if (endDate != null)
            days = endDate.getDayOfYear() - this.startDate.getDayOfYear() + 1;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        if (startDate != null) {
            days = (int) ChronoUnit.DAYS.between(startDate, endDate);

        }

    }

    public DateTimeRange getIntervalOfDriver(Driver driver) {
        for (DriverTourIntervals dti : driverIntervals) {
            if (dti.getDriver().equals(driver)) {
                try {
                    return dti.getInterval();
                } catch (ValidationException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public DateTimeRange getIntervalOfGuide(Guide guide) {
        for (GuideTourIntervals gti : guideIntervals) {
            if (gti.getGuide().equals(guide)) {
                try {
                    return gti.getInterval();
                } catch (ValidationException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public int getTouristsCount() {
        return touristsCount;
    }

    public void setTouristsCount(int touristsCount) {
        this.touristsCount = touristsCount;
    }

    public DateTimeRange getRange() {
        if (startDate != null && endDate != null) {
            try {
                return new DateTimeRange(startDate, endDate);
            } catch (ValidationException e) {
                // ignored
            }
        }
        return null;
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

    @Override
    public String toString() {
        return "Tour {" +
                "\n     id=" + id +
                ",\n    tittle='" + tittle + '\'' +
                ",\n    date=" + getRange().toString() +
                ",\n    fileName='" + fileName + '\'' +
                ",\n    days=" + days +
                ",\n    touristsCount=" + touristsCount +
                ",\n    desc="+description+
                ",\n    drivers="+getDriversAsString()+
                ",\n    guides="+getGuidesAsString() +
                ",\n    createdBy='" + createdBy + '\'' +
                ",\n    creationDate=" + creationDate +
                ",\n    lastModifiedBy='" + lastModifiedBy + '\'' +
                ",\n    lasModifyDate=" + lasModifyDate +
                "\n}";
    }

    private String getDriversAsString() {
        var res = "[";
        for (Driver d : drivers) {
            var i = getIntervalOfDriver(d);
            res += d.toString(i) + "\n";
        }
        res += "]";
        return res;
    }

    private String getGuidesAsString() {
        var res = "[";
        for (Guide d : guides) {
            var i = getIntervalOfGuide(d);
            res += d.toString(i) + "\n";
        }
        res += "]";
        return res;
    }
}
