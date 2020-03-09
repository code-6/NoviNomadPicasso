package stanislav.tun.novinomad.picasso.persistance.pojos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

public class ConcurrentHolder {
    Logger logger = LoggerFactory.getLogger(ConcurrentHolder.class);
    private Map<Driver, User> driversHolder = new HashMap<>();
    private Map<Guide, User> guidesHolder = new HashMap<>();
    private Map<Tour, User> toursHolder = new HashMap<>();

    public void hold(AbstractEntity entity, User user) {
        if (!isHold(entity))
            if (entity instanceof Driver) {
                driversHolder.put((Driver) entity, user);
                logger.debug("Hold driver " + ((Driver) entity).getFullName() + " by user " + user.getUserName());
            } else if (entity instanceof Guide) {
                guidesHolder.put((Guide) entity, user);
                logger.debug("Hold guide " + ((Guide) entity).getFullName() + " by user " + user.getUserName());
            } else if (entity instanceof Tour) {
                toursHolder.put((Tour) entity, user);
                logger.debug("Hold tour " + ((Tour) entity).getTittle() + " by user " + user.getUserName());
            }
    }

    public void release(AbstractEntity entity) {
        if (entity instanceof Driver) {
            driversHolder.remove((Driver) entity);
            logger.debug("Release driver " + ((Driver) entity).getFullName());
        } else if (entity instanceof Guide) {
            guidesHolder.remove((Guide) entity);
            logger.debug("Release guide " + ((Guide) entity).getFullName());
        } else if (entity instanceof Tour) {
            toursHolder.remove(entity);
            logger.debug("Release tour " + ((Tour) entity).getTittle());
        }
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
