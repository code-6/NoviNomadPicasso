package stanislav.tun.novinomad.picasso.util;

import org.joda.time.Interval;
import org.joda.time.LocalDateTime;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;

public abstract class Validator {

    public static boolean overlaps(Driver driver, LocalDateTime start, LocalDateTime end){
        var interval = new Interval(start.toDateTime(), end.toDateTime());
        for (Tour t : driver.getTours()) {
            var dInterval = new Interval(t.getStartDate().toDateTime(), t.getEndDate().toDateTime());
            if(interval.overlaps(dInterval))
                return true;
        }
        return false;
    }

}
