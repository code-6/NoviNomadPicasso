package stanislav.tun.novinomad.picasso.persistance.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import stanislav.tun.novinomad.picasso.persistance.pojos.Guide;

import java.util.Collection;

@Repository
public interface IGuideRepo extends CrudRepository<Guide, Long> {
    Collection<Guide> findByDeletedFalse();

    Guide findByFullName(String name);
}
