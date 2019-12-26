package stanislav.tun.novinomad.picasso.persistance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.repositories.IDriverRepo;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
public class DriverService {
    @Autowired
    IDriverRepo repo;

    @Transactional
    public void createDriver(Driver driver){
        repo.save(driver);
    }

    @Transactional
    public Collection<Driver> getDriversList(){
        return repo.findAll();
    }

    @Transactional
    public Optional<Driver> getDriver(long id){
        return repo.findById(id);
    }

    @Transactional
    public Driver getDriver(String name){
        var drivers = getDriversList();
        for (Driver d: drivers) {
            if(d.getName().equals(name))
                return d;
        }
        // todo: return null or throw an exception?
        return null;
    }

}
