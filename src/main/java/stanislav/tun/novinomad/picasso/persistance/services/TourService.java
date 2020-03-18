package stanislav.tun.novinomad.picasso.persistance.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.pojos.Guide;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;
import stanislav.tun.novinomad.picasso.persistance.repositories.ITourRepo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class TourService {

    Logger logger = LoggerFactory.getLogger(TourService.class);

    @Autowired
    ITourRepo repo;

    public Tour createOrUpdateTour(Tour tour) {
        return repo.save(tour);
    }

    public boolean exist(Tour t){
        return repo.existsById(t.getId());
    }

    public boolean exist(long id){
        return repo.existsById(id);
    }

    public boolean exist(String title){
        return repo.existsByTittle(title);
    }

    public List<Tour> getToursRelated2Driver(Long driverId, int month, int year) {
        return (List<Tour>) repo.findToursByMonthAndYearAndDriver(month, year, driverId);
    }

    public List<Tour> getToursRelated2Guide(Long guideId, int month, int year){
        return (List<Tour>) repo.findToursByMonthAndYearAndGuide(month, year, guideId);
    }

    public Optional<Tour> getTour(Long id) {
        return repo.findById(id);
    }

    public List<Tour> getAllTours() {
        return repo.findAll();
    }

    public List<Tour> getToursForDate(int month, int year) {
        return (List<Tour>) repo.findToursByMonthAndYear(month, year);
    }

    public List<Tour> getToursByYear(int year){
        return (List<Tour>) repo.findToursByYear(year);
    }

    public Tour getTour(String tittle) {
        var allTours = getAllTours();
        for (Tour t : allTours) {
            if (t.getTittle().toLowerCase().equals(tittle.toLowerCase()))
                return t;
        }
        return null;
    }

    public Set<Driver> getAttachedDrivers(Long id) {
        var tour = getTour(id);
        if (!tour.isEmpty() && tour.isPresent())
            return tour.get().getDrivers();
        return null;
    }

    public Set<Guide> getAttachedGuides(long id) {
        var tour = getTour(id);
        if (!tour.isEmpty() && tour.isPresent())
            return tour.get().getGuides();
        return null;
    }
}
