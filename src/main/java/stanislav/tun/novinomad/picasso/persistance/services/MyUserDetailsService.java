package stanislav.tun.novinomad.picasso.persistance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import stanislav.tun.novinomad.picasso.persistance.repositories.IUserRepo;
import stanislav.tun.novinomad.picasso.security.MyUserDetails;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private IUserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepo.findByUserName(username);
        if(!user.isEmpty() && user.isPresent())
            return new MyUserDetails(user.get());
        else
            throw new UsernameNotFoundException("User '"+username+"' not found");
    }
}
