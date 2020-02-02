package stanislav.tun.novinomad.picasso.persistance.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import stanislav.tun.novinomad.picasso.persistance.pojos.Guide;

@Repository
public interface IGuideRepo extends CrudRepository<Guide, Long> {
    Guide findByFullName(String name);
}
