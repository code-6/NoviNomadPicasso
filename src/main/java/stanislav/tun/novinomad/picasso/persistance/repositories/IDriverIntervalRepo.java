package stanislav.tun.novinomad.picasso.persistance.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.pojos.DriverTourIntervals;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;

import java.util.Collection;

@Repository
public interface IDriverIntervalRepo extends CrudRepository<DriverTourIntervals, Long> {
    Collection<DriverTourIntervals> findByDriverEquals(Driver driver);
    Collection<DriverTourIntervals> findByDriverAndTourEquals(Driver driver, Tour tour);
    Collection<DriverTourIntervals> findByTourEquals(Tour tour);

}
