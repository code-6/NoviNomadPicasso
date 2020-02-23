package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.sun.istack.NotNull;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
//@JsonRootName(value = "guide_intervals")
public class GuideTourIntervals extends AbstractIntervals {
    @NotNull
    @ManyToOne
    //@JsonProperty
    Guide guide;

    public GuideTourIntervals() {
    }

    public GuideTourIntervals(Tour tour, DateTimeRange interval, Guide guide) {
        super(tour, interval);
        this.guide = guide;
    }

    public Guide getGuide() {
        return guide;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
    }
}
