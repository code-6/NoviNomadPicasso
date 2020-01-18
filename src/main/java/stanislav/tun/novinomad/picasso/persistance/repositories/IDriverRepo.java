package stanislav.tun.novinomad.picasso.persistance.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;

@Repository
public interface IDriverRepo extends CrudRepository<Driver, Long> {
    void findByFullName(String name);
}
