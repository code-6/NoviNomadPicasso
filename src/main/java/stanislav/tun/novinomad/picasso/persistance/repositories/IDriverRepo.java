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

}
