package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.sun.istack.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.xml.bind.ValidationException;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
//@JsonRootName(value = "driver_intervals")
public class DriverTourIntervals extends AbstractIntervals {
    @NotNull
    @ManyToOne
    private Driver driver;

    public DriverTourIntervals() {
    }

    public DriverTourIntervals(Tour tour, DateTimeRange interval, Driver driver) {
        super(tour, interval);
        this.driver = driver;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

}
