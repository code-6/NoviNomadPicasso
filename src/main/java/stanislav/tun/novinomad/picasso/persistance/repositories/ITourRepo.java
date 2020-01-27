package stanislav.tun.novinomad.picasso.persistance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;

import java.util.Collection;
import java.util.List;

public interface ITourRepo extends JpaRepository<Tour, Long> {
    Tour findByTittle(String tittle);

}
