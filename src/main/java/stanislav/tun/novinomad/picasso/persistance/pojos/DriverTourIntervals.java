package stanislav.tun.novinomad.picasso.persistance.pojos;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class DriverTourIntervals extends AbstractIntervals {

    @ManyToOne(targetEntity = Driver.class)
    private Driver driver;

    public DriverTourIntervals() {
    }

    public DriverTourIntervals(Driver driver) {
        this.driver = driver;
    }

    public DriverTourIntervals(Tour tour, MyInterval interval, Driver driver) {
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
