package stanislav.tun.novinomad.picasso.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import stanislav.tun.novinomad.picasso.persistance.pojos.*;
import stanislav.tun.novinomad.picasso.persistance.services.DriverService;
import stanislav.tun.novinomad.picasso.persistance.services.GuideService;
import stanislav.tun.novinomad.picasso.persistance.services.TourService;

import java.util.HashMap;
import java.util.Map;

public class ConcurrentHolder {
    private Logger logger = LoggerFactory.getLogger(ConcurrentHolder.class);
    private Map<Driver, User> driversHolder = new HashMap<>();
    private Map<Guide, User> guidesHolder = new HashMap<>();
    private Map<Tour, User> toursHolder = new HashMap<>();
    private Map<User, Integer> yearHolder = new HashMap<>();
    private Map<User, Integer> monthHolder = new HashMap<>();

    @Autowired
    private DriverService driverService;

    @Autowired
    private GuideService guideService;

    @Autowired
    private TourService tourService;

    public void hold(AbstractEntity entity, User user) {
//        if (!isHold(entity))
        if (entity instanceof Driver) {
            driversHolder.put((Driver) entity, user);
            logger.debug("Hold driver: " + ((Driver) entity).getFullName() + " by user: " + user.getUserName());
        } else if (entity instanceof Guide) {
            guidesHolder.put((Guide) entity, user);
            logger.debug("Hold guide: " + ((Guide) entity).getFullName() + " by user: " + user.getUserName());
        } else if (entity instanceof Tour) {
            toursHolder.put((Tour) entity, user);
            logger.debug("Hold tour: " + ((Tour) entity).getTittle() + " by user: " + user.getUserName());
        }
    }

    public int getYearByUser(User user) {
        int result = -1;
        if (yearHolder.containsKey(user))
            result = yearHolder.get(user);
        return result;
    }

    public int getMonthByUser(User user) {
        int res = -1;
        if (monthHolder.containsKey(user))
            res = monthHolder.get(user);

        return res;
    }

    public void hold(Integer year, User user) {
        yearHolder.put(user, year);
        logger.debug("Hold year: " + year + " user: " + user.getUserName());
    }

    public void hold(Integer month, Integer year, User user) {
        if (month != null)
            yearHolder.put(user, year);
        if (year != null)
            monthHolder.put(user, month);

        logger.debug("Hold year: " + year + " month: " + month + " user: " + user.getUserName());
    }

    /**
     * release one entity no matter which user holds.
     * always use before save to database!
     */
    public void release(AbstractEntity entity) {
        if (entity instanceof Driver) {
            driversHolder.remove(driverService.getDriver(entity.getId()).get());
            logger.debug("Release driver: " + ((Driver) entity).getFullName());
        } else if (entity instanceof Guide) {
            guidesHolder.remove(guideService.getGuide(entity.getId()).get());
            logger.debug("Release guide: " + ((Guide) entity).getFullName());
        } else if (entity instanceof Tour) {
            toursHolder.remove(tourService.getTour(entity.getId()).get());
            logger.debug("Release tour: " + ((Tour) entity).getTittle());
        }
    }

    /**
     * release all entities holds by particular user
     */
    public void release(User user) {
        if (driversHolder.containsValue(user))
            for (Driver d : driversHolder.keySet())
                if (getHolderOf(d).equals(user))
                    release(d);

        if (guidesHolder.containsValue(user))
            for (Guide g : guidesHolder.keySet())
                if (getHolderOf(g).equals(user))
                    release(g);

        if (toursHolder.containsValue(user))
            for (Tour t : toursHolder.keySet())
                if (getHolderOf(t).equals(user))
                    release(t);
    }

    /**
     * release all entities. Danger method!
     */
    public void release() {
        driversHolder.clear();
        guidesHolder.clear();
        toursHolder.clear();
    }

    public boolean isHold(AbstractEntity entity) {
        if (entity instanceof Driver) {
            return driversHolder.keySet().contains((Driver) entity);
        } else if (entity instanceof Guide) {
            return guidesHolder.keySet().contains((Guide) entity);
        } else if (entity instanceof Tour) {
            return toursHolder.keySet().contains((Tour) entity);
        }
        return false;
    }

    /**
     * @param entity under hold entity
     * @return holder of particular entity as User type
     */
    public User getHolderOf(AbstractEntity entity) {
        if (entity instanceof Driver) {
            return driversHolder.get(entity);
        } else if (entity instanceof Guide) {
            return guidesHolder.get(entity);
        } else if (entity instanceof Tour) {
            return toursHolder.get(entity);
        }
        return null;
    }

    public Map<Driver, User> getDriversHolder() {
        return driversHolder;
    }

    public Map<Guide, User> getGuidesHolder() {
        return guidesHolder;
    }

    public Map<Tour, User> getToursHolder() {
        return toursHolder;
    }
}
