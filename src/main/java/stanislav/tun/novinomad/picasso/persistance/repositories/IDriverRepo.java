package stanislav.tun.novinomad.picasso.persistance.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;

import java.util.Collection;

@Repository
public interface IDriverRepo extends CrudRepository<Driver, Long> {

    Collection<Driver> findByDeletedFalse();

    Driver findByFullName(String name);

    @Query(value = "select t.* from tours as t " +
            "join tours_drivers as td on t.id = td.tour_id " +
            "where td.driver_id = :driverId " +
            "and ( trunc(t.start_date) >= trunc(to_char(sysdate, 'YYYY-MM-DD HH:MM:SS')) " +
            "or trunc(t.end_date) >= trunc(to_char(sysdate, 'YYYY-MM-DD HH:MM:SS')))",
            nativeQuery = true)
    Collection<Tour> findDriverFutureTours(@Param("driverId") long driverId);

}
