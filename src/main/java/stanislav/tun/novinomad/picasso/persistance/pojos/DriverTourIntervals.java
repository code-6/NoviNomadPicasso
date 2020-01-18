package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.fasterxml.jackson.annotation.JsonRootName;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@JsonRootName(value = "driver_intervals")
public class DriverTourIntervals extends AbstractIntervals {

    @NotNull
    @ManyToOne
    private Driver driver;

    public DriverTourIntervals() {
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
