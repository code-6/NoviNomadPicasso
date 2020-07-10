package stanislav.tun.novinomad.picasso.persistance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import stanislav.tun.novinomad.picasso.persistance.pojos.User;

import java.util.Optional;

public interface IUserRepo extends CrudRepository<User, Long> {
    Optional<User> findByUserName(String userName);
}
