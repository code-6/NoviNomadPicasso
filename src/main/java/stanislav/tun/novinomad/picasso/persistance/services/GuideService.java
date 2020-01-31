package stanislav.tun.novinomad.picasso.persistance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stanislav.tun.novinomad.picasso.persistance.pojos.Guide;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;
import stanislav.tun.novinomad.picasso.persistance.repositories.IGuideRepo;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GuideService {
    @Autowired
    IGuideRepo repo;

    @Transactional
    public void createOrUpdateGuide(Guide guide){
        repo.save(guide);
    }

    @Transactional
    public List<Guide> getGuidesList(){
        return (List<Guide>) repo.findAll();
    }

    @Transactional
    public Optional<Guide> getGuide(long id){
        return repo.findById(id);
    }

    /**
     * @param name: can be a first, middle, or last name of the guide
     * todo: make method more smart, find by any entered text. f+m, f+l, f+m+l etc.
     * */
    @Transactional
    public Guide getGuide(String name){
        return repo.findByFullName(name);
    }

    public List<Tour> getToursRelatedTo(Long guideId){
        var guide = getGuide(guideId);
        return (List<Tour>) guide.get().getTours();
    }

    public List<Tour> getToursRelatedTo(Long guideId, LocalDate from, LocalDate to){
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
}
