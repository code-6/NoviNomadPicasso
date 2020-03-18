package stanislav.tun.novinomad.picasso.persistance.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.pojos.Guide;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;
import stanislav.tun.novinomad.picasso.persistance.repositories.IGuideRepo;
import stanislav.tun.novinomad.picasso.persistance.repositories.ITourRepo;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class GuideService {
    Logger logger = LoggerFactory.getLogger(DriverService.class);

    @Autowired
    IGuideRepo repo;

    @Autowired
    ITourRepo tourRepo;


    public Guide createOrUpdateGuide(Guide guide){
        return repo.save(guide);
    }

    public List<Guide> getAllGuides(){
        return (List<Guide>) repo.findAll();
    }

    public List<Guide> getGuidesList(){
        return (List<Guide>) repo.findByDeletedFalse();
    }

    public Optional<Guide> getGuide(long id){
        return repo.findById(id);
    }

    /**
     * @param name: can be a first, middle, or last name of the guide
     * todo: make method more smart, find by any entered text. f+m, f+l, f+m+l etc.
     * */
    public Guide getGuide(String name){
        return repo.findByFullName(name);
    }

    public List<Tour> getToursRelatedTo(Long guideId){
        var guide = getGuide(guideId);
        return (List<Tour>) guide.get().getTours();
    }

    public boolean hasFutureTours(Guide guide){
        Collection<Tour> tours = tourRepo.findGuideFutureTours(guide.getId());
        if(tours != null)
            return !tours.isEmpty();

        return true;
    }

    public Collection<Tour> getGuideFutureTours(Guide guide){
        return tourRepo.findGuideFutureTours(guide.getId());
    }

    public List<Tour> getToursRelatedTo(Long guideId, LocalDateTime from, LocalDateTime to){
        var guide = getGuide(guideId);
        var tours = (List<Tour>) guide.get().getTours();
        var list = new ArrayList<Tour>();
        for (Tour t : tours) {
            if ( (t.getStartDate().isEqual(from) || t.getStartDate().isAfter(from)) &&
                    (t.getEndDate().isEqual(to) || t.getEndDate().isBefore(to)) ){
                list.add(t);
            }
        }
        return list;
    }

    public boolean exist(Guide guide){
        return repo.existsById(guide.getId());
    }

    public boolean exist(Long id){
        return repo.existsById(id);
    }
}
