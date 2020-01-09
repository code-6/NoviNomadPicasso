package stanislav.tun.novinomad.picasso.persistance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.repositories.IDriverRepo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class  DriverService {
    @Autowired
    IDriverRepo repo;

    @Transactional
    public void createOrUpdateDriver(Driver driver){
        repo.save(driver);
    }

    @Transactional
    public List<Driver> getDriversList(){
        return (List<Driver>) repo.findAll();
    }

    @Transactional
    public Optional<Driver> getDriver(long id){
        return repo.findById(id);
    }

    /**
     * @param name: can be a first, middle, or last name of the driver
     * todo: make method more smart, find by any entered text. f+m, f+l, f+m+l etc.
     * */
    @Transactional
    public Driver getDriver(String name){
        var drivers = getDriversList();
        for (Driver d: drivers) {
            if(d.getFirstName().equals(name) || d.getMiddleName().equals(name) || d.getLastName().equals(name))
                return d;
        }
        // todo: return null or throw an exception?
        return null;
    }

}
