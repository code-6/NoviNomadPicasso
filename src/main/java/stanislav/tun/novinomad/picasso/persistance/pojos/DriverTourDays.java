package stanislav.tun.novinomad.picasso.persistance.pojos;

import java.util.HashMap;
import java.util.Map;

public class DriverTourDays {

    private Tour tour;
    private Map<Driver, String> driversDays = new HashMap<>();

    public void addDriverDays(Driver driver, String days){
        driversDays.put(driver,days);
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public Map<Driver, String> getDriversDays() {
        return driversDays;
    }

    public void setDriversDays(Map<Driver, String> driversDays) {
        this.driversDays = driversDays;
    }
}
