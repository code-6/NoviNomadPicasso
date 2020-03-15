package stanislav.tun.novinomad.picasso.persistance.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stanislav.tun.novinomad.picasso.persistance.pojos.Guide;
import stanislav.tun.novinomad.picasso.persistance.pojos.GuideTourIntervals;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;
import stanislav.tun.novinomad.picasso.persistance.repositories.IGuideIntervalRepo;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
public class GuideIntervalService {
    Logger logger = LoggerFactory.getLogger(GuideIntervalService.class);

    @Autowired
    IGuideIntervalRepo repo;

    public void createOrUpdateInterval( GuideTourIntervals interval) {

        repo.save(interval);
        //logger.debug("created "+ JsonPrinter.getString(interval));
    }


    public Optional<GuideTourIntervals> getInterval(long id) {
        return repo.findById(id);
    }


    public Collection<GuideTourIntervals> getAllRelatedToGuide(Guide guide) {
        //return Collections.sort(Arrays.asList(repo.findByDriverEquals(driverId)));
        return repo.findByGuideEquals(guide);
    }


    public Collection<GuideTourIntervals> getAllRelatedToTour(Tour tour) {
        //return Collections.sort(Arrays.asList(repo.findByDriverEquals(driverId)));
        return repo.findByTourEquals(tour);
    }


    public Collection<GuideTourIntervals> getAllRelatedToTourAndGuide(Tour tour, Guide guide) {
        //return Collections.sort(Arrays.asList(repo.findByDriverEquals(driverId)));
        return repo.findByGuideAndTourEquals(guide, tour);
    }

    public Collection<GuideTourIntervals> getAllIntervals() {
        return (Collection<GuideTourIntervals>) repo.findAll();
    }


    public void delete(GuideTourIntervals guideTourIntervals){
        repo.delete(guideTourIntervals);
    }

    public void delete(Long id){
        repo.deleteById(id);
    }
}
