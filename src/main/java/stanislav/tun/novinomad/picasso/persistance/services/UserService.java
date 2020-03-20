package stanislav.tun.novinomad.picasso.persistance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import stanislav.tun.novinomad.picasso.persistance.pojos.User;
import stanislav.tun.novinomad.picasso.persistance.repositories.IUserRepo;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Autowired
    private IUserRepo userRepo;

    public void createUser(User user) {
        userRepo.save(user);
    }

    public Optional<User> getUser(String userName) {
        return userRepo.findByUserName(userName);
    }

    public Optional<User> getUser(Long id){
        return userRepo.findById(id);
    }
}
