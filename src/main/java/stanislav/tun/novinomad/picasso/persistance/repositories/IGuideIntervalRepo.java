package stanislav.tun.novinomad.picasso.persistance.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import stanislav.tun.novinomad.picasso.persistance.pojos.Guide;
import stanislav.tun.novinomad.picasso.persistance.pojos.GuideTourIntervals;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;

import java.util.Collection;

@Repository
public interface IGuideIntervalRepo extends CrudRepository<GuideTourIntervals, Long> {
    Collection<GuideTourIntervals> findByGuideEquals(Guide guide);
    Collection<GuideTourIntervals> findByGuideAndTourEquals(Guide guide, Tour tour);
    Collection<GuideTourIntervals> findByTourEquals(Tour tour);

}
