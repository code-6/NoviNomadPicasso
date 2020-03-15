package stanislav.tun.novinomad.picasso.persistance.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stanislav.tun.novinomad.picasso.persistance.pojos.Guide;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;
import stanislav.tun.novinomad.picasso.persistance.repositories.IGuideRepo;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class GuideService {
    Logger logger = LoggerFactory.getLogger(DriverService.class);

    @Autowired
    IGuideRepo repo;


    public void createOrUpdateGuide(Guide guide){
        var u = UUID.randomUUID().toString();
        if(exist(guide))
            logger.info(u+" edit "+ guide.toString());
        else
            logger.info(u+" create "+guide.toString());

        repo.save(guide);
        logger.info(u+" success");
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
