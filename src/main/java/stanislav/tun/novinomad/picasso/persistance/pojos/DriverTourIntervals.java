package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.sun.istack.NotNull;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
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
