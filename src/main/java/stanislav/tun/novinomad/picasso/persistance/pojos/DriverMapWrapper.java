package stanislav.tun.novinomad.picasso.persistance.pojos;

import java.util.HashMap;
import java.util.Map;

public class DriverMapWrapper {
    private Map<Driver, String> map = new HashMap<>();

    public Map<Driver, String> getMap() {
        return map;
    }

    public void setMap(Map<Driver, String> map) {
        this.map = map;
    }
}
