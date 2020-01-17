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
    private LocalDateTime from;
    @JsonIgnore
    private LocalDateTime to;

    @NotNull
    @Transient
    private MyInterval interval;

    public AbstractIntervals() {
    }

    public AbstractIntervals(Tour tour, MyInterval interval) {
        this.tour = tour;
        this.interval = interval;
        from = interval.getStart();
        to = interval.getEnd();
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }

    public MyInterval getInterval() {
        return interval;
    }

    public void setInterval(MyInterval interval) {
        this.interval = interval;
    }
}
