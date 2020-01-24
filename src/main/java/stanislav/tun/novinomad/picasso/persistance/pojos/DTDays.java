package stanislav.tun.novinomad.picasso.persistance.pojos;

import java.util.HashMap;
import java.util.Map;

public class DTDays {
    private Map<Driver, String> driversDays = new HashMap<>();

    public Map<Driver, String> getDriversDays() {
        return driversDays;
    }

    public void setDriversDays(Map<Driver, String> driversDays) {
        this.driversDays = driversDays;
    }
}
