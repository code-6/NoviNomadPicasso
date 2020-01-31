package stanislav.tun.novinomad.picasso.persistance.repositories;

import org.springframework.data.repository.CrudRepository;
import stanislav.tun.novinomad.picasso.persistance.pojos.Guide;

public interface IGuideRepo extends CrudRepository<Guide, Long> {
    Guide findByFullName(String name);
}
