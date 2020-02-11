package stanislav.tun.novinomad.picasso.persistance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;
import stanislav.tun.novinomad.picasso.persistance.repositories.IDriverRepo;
import stanislav.tun.novinomad.picasso.persistance.repositories.ITourRepo;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
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
        return repo.findByFullName(name);
    }

    public List<Tour> getToursRelatedTo(Long driverId){
        var driver = getDriver(driverId);
        return (List<Tour>) driver.get().getTours();
    }

    public List<Tour> getToursRelatedTo(Long driverId, LocalDate from, LocalDate to){
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

}
