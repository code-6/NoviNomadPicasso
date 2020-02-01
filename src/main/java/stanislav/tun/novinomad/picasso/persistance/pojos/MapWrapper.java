package stanislav.tun.novinomad.picasso.persistance.pojos;

import java.util.HashMap;
import java.util.Map;

public class MapWrapper {
    private Map<Driver, String> driverMap = new HashMap<>();
    private Map<Guide, String> guideMap = new HashMap<>();

    public Map<Driver, String> getDriverMap() {
        return driverMap;
    }

    public void setDriverMap(Map<Driver, String> driverMap) {
        this.driverMap = driverMap;
    }

    public Map<Guide, String> getGuideMap() {
        return guideMap;
    }

    public void setGuideMap(Map<Guide, String> guideMap) {
        this.guideMap = guideMap;
    }
}
