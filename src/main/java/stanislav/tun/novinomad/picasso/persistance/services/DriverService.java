package stanislav.tun.novinomad.picasso.persistance.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;
import stanislav.tun.novinomad.picasso.persistance.repositories.IDriverRepo;
import stanislav.tun.novinomad.picasso.persistance.repositories.ITourRepo;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class  DriverService {
    Logger logger = LoggerFactory.getLogger(DriverService.class);

    @Autowired
    IDriverRepo repo;

    @Autowired
    private ITourRepo tourRepo;

    //@CacheEvict(value = "drivers", allEntries = true)
    //@CachePut(value = "drivers")
    public Driver createOrUpdateDriver(Driver driver){
        return repo.save(driver);
    }

    public List<Driver> getDriversList(){
        return (List<Driver>) repo.findByDeletedFalse();
    }

    public List<Driver> getAllDrivers(){
        return (List<Driver>) repo.findAll();
    }

    public Optional<Driver> getDriver(long id){
        return repo.findById(id);
    }

    /**
     * @param name: can be a first, middle, or last name of the driver
     * todo: make method more smart, find by any entered text. f+m, f+l, f+m+l etc.
     * */
    public Driver getDriver(String name){
        return repo.findByFullName(name);
    }

    public List<Tour> getToursRelatedTo(Long driverId){
        var driver = getDriver(driverId);
        return (List<Tour>) driver.get().getTours();
    }

    public List<Tour> getToursRelatedTo(Long driverId, LocalDateTime from, LocalDateTime to){
        var driver = getDriver(driverId);
        var tours = (List<Tour>) driver.get().getTours();
        var list = new ArrayList<Tour>();
        for (Tour t : tours) {
            if ( (t.getStartDate().isEqual(from) || t.getStartDate().isAfter(from)) &&
                    (t.getEndDate().isEqual(to) || t.getEndDate().isBefore(to)) ){
                list.add(t);
            }
        }
        return list;
    }

    public boolean hasFutureTours(Driver driver){
        Collection<Tour> tours = tourRepo.findDriverFutureTours(driver.getId());
        if(tours != null)
            return !tours.isEmpty();

        return true;
    }

    public Collection<Tour> getDriverFutureTours(Driver driver){
        return tourRepo.findDriverFutureTours(driver.getId());
    }

    public boolean exist(Driver driver){
        return repo.existsById(driver.getId());
    }

}
