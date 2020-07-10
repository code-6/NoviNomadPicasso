package stanislav.tun.novinomad.picasso.persistance.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from driver_tour_intervals gti where gti.tour_id = :tourId and gti.driver_id = :driverId")
    void delete(@Param("tourId") long tourId, @Param("driverId") long driverId);

}
