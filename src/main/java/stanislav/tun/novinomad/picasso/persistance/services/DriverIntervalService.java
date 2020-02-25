package stanislav.tun.novinomad.picasso.persistance.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.pojos.DriverTourIntervals;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;
import stanislav.tun.novinomad.picasso.persistance.repositories.IDriverIntervalRepo;
import stanislav.tun.novinomad.picasso.util.JsonPrinter;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
public class DriverIntervalService {

    Logger logger = LoggerFactory.getLogger(DriverIntervalService.class);

    @Autowired
    IDriverIntervalRepo repo;

    @Transactional
    public void createOrUpdateInterval(DriverTourIntervals interval) {
        logger.info("created " + JsonPrinter.getString(interval));
        repo.save(interval);
    }

    @Cacheable("singleInterval")
    public Optional<DriverTourIntervals> getInterval(long id) {
        return repo.findById(id);
    }

    @Cacheable("driverIntervals")
    public Collection<DriverTourIntervals> getAllRelatedToDriver(Driver driver) {
        //return Collections.sort(Arrays.asList(repo.findByDriverEquals(driverId)));
        return repo.findByDriverEquals(driver);
    }

    @Cacheable("driverTourIntervals")
    public Collection<DriverTourIntervals> getAllRelatedToTour(Tour tour) {
        //return Collections.sort(Arrays.asList(repo.findByDriverEquals(driverId)));
        return repo.findByTourEquals(tour);
    }

    @Cacheable("tourDriverIntervals")
    public Collection<DriverTourIntervals> getAllRelatedToTourAndDriver(Tour tour, Driver driver) {
        //return Collections.sort(Arrays.asList(repo.findByDriverEquals(driverId)));
        return repo.findByDriverAndTourEquals(driver, tour);
    }

    @Cacheable("allDriverIntervals")
    public Collection<DriverTourIntervals> getAllIntervals() {
        return (Collection<DriverTourIntervals>) repo.findAll();
    }

    public void delete(DriverTourIntervals driverTourIntervals) {
        repo.delete(driverTourIntervals);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
