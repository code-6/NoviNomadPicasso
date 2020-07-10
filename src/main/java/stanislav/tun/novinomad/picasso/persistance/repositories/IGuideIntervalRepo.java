package stanislav.tun.novinomad.picasso.persistance.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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

    @Modifying
    @Query(nativeQuery = true,
    value = "delete from guide_tour_intervals gti where gti.tour_id = :tourId and gti.guide_id = :guideId")
    void delete(@Param("tourId") long tourId, @Param("guideId") long guideId);

}
