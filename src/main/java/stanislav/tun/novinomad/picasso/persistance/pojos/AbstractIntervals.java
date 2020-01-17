package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class AbstractIntervals extends AbstractEntity {
    @NotNull
    @ManyToOne
    private Tour tour;
    @JsonIgnore
    private LocalDateTime startDate;
    @JsonIgnore
    private LocalDateTime endDate;

    @NotNull
    @Transient
    private MyInterval interval;

    public AbstractIntervals() {
    }

    public AbstractIntervals(Tour tour, MyInterval interval) {
        this.tour = tour;
        this.interval = interval;
        startDate = interval.getStart();
        endDate = interval.getEnd();
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

    public MyInterval getInterval() {
        return interval;
    }

    public void setInterval(MyInterval interval) {
        this.interval = interval;
    }
}
