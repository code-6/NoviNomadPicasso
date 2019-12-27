package stanislav.tun.novinomad.picasso.persistance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;
import stanislav.tun.novinomad.picasso.persistance.repositories.ITourRepo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TourService {
    @Autowired
    ITourRepo repo;

    public void createTour(Tour tour) {
        repo.save(tour);
    }

    public Optional<Tour> getTour(Long id) {
        return repo.findById(id);
    }

    public List<Tour> getAllTours() {
        return repo.findAll();
    }

    public Tour getTour(String name) {
        var allTours = getAllTours();
        for (Tour t : allTours) {
            if (t.getTittle().toLowerCase().equals(name.toLowerCase()))
                return t;
        }
        return null;
    }
}
