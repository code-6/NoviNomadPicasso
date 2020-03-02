package stanislav.tun.novinomad.picasso.persistance.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.pojos.Guide;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;
import stanislav.tun.novinomad.picasso.persistance.repositories.ITourRepo;
import stanislav.tun.novinomad.picasso.util.JsonPrinter;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class TourService {

    Logger logger = LoggerFactory.getLogger(TourService.class);
    @Autowired
    ITourRepo repo;

    public void createOrUpdateTour(Tour tour) {
        if(repo.existsById(tour.getId()))
            logger.info("edit "+ JsonPrinter.getString(tour));
        else
            logger.info("create "+ JsonPrinter.getString(tour));

        repo.save(tour);
    }

    public Optional<Tour> getTour(Long id) {
        return repo.findById(id);
    }

    public List<Tour> getAllTours() {
        return repo.findAll();
    }

    public Tour getTour(String tittle) {
        var allTours = getAllTours();
        for (Tour t : allTours) {
            if (t.getTittle().toLowerCase().equals(tittle.toLowerCase()))
                return t;
        }
        return null;
    }

    public Set<Driver> getAttachedDrivers(Long id){
        var tour = getTour(id);
        if(!tour.isEmpty() && tour.isPresent())
            return tour.get().getDrivers();
        return null;
    }

    public Set<Guide> getAttachedGuides(long id) {
        var tour = getTour(id);
        if(!tour.isEmpty() && tour.isPresent())
            return tour.get().getGuides();
        return null;
    }
}
