package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.sun.istack.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
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
