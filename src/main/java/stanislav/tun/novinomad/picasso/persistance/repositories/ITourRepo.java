package stanislav.tun.novinomad.picasso.persistance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;

public interface ITourRepo extends JpaRepository<Tour, Long> {
    Tour findByTittle(String tittle);

}
