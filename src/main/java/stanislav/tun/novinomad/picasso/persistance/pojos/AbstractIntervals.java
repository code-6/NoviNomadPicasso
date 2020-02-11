package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.LocalDate;
import java.util.Objects;

@MappedSuperclass
public abstract class AbstractIntervals {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @NotNull
    @ManyToOne
    //@JsonIgnore
    private Tour tour;
    //@JsonIgnore
    private LocalDate startDate;
    //@JsonIgnore
    private LocalDate endDate;

    @NotNull
    @Transient
   // @JsonProperty
    private MyInterval interval;

    public AbstractIntervals() {
    }

    public AbstractIntervals(Tour tour, MyInterval interval) {
        this.tour = tour;
        this.interval = interval;
        startDate = interval.getStart();
        endDate = interval.getEnd();
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractIntervals)) return false;
        AbstractIntervals that = (AbstractIntervals) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate from) {
        this.startDate = from;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate to) {
        this.endDate = to;
    }

    public MyInterval getInterval() throws ValidationException {
        if(interval == null)
            return new MyInterval(startDate, endDate);
        return interval;
    }

    public void setInterval(MyInterval interval) {
        this.interval = interval;
        startDate = interval.getStart();
        endDate = interval.getEnd();
    }
}
