package stanislav.tun.novinomad.picasso.persistance.pojos;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
public abstract class AbstractIntervals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @NotNull
    @ManyToOne
    //@JsonIgnore
    private Tour tour;
    //@JsonIgnore
    private LocalDateTime startDate;
    //@JsonIgnore
    private LocalDateTime endDate;

    @NotNull
    @Transient
    // @JsonProperty
    private DateTimeRange interval;

    public AbstractIntervals() {
    }

    public AbstractIntervals(Tour tour, DateTimeRange interval) {
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime from) {
        this.startDate = from;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime to) {
        this.endDate = to;
    }

    public DateTimeRange getInterval() throws ValidationException {
        if (interval == null)
            return new DateTimeRange(startDate, endDate);
        return interval;
    }

    public void setInterval(DateTimeRange interval) {
        this.interval = interval;
        startDate = interval.getStart();
        endDate = interval.getEnd();
    }
}
