package stanislav.tun.novinomad.picasso.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import stanislav.tun.novinomad.picasso.persistance.pojos.Guide;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;
import stanislav.tun.novinomad.picasso.persistance.pojos.User;
import stanislav.tun.novinomad.picasso.persistance.services.GuideService;
import stanislav.tun.novinomad.picasso.persistance.services.UserService;
import stanislav.tun.novinomad.picasso.util.ConcurrentHolder;

import java.util.UUID;


@Controller
@RequestMapping("/guides")
public class GuideController {
    @Autowired
    private GuideService guideService;

    @Autowired
    private ConcurrentHolder holder;

    @Autowired
    private AuditorAware auditor;

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(GuideController.class);

    @RequestMapping("/list")
    public ModelAndView getGuidesListView() {
        User user = userService.getUser(auditor.getCurrentAuditor().get().toString()).get();
        holder.release(user);
        var guides = guideService.getGuidesList();
        var modelAndView = new ModelAndView();
        modelAndView.addObject("guides", guides);
        modelAndView.setViewName("guidesListPage");
        modelAndView.addObject("activeGuides", true);
        return modelAndView;
    }

    // get view
    @RequestMapping("/add")
    public ModelAndView addGuideView() {
        var guide = new Guide();
        var modelAndView = new ModelAndView();
        modelAndView.addObject("guide", guide);
        modelAndView.setViewName("addGuidePage");
        return modelAndView;
    }

    @RequestMapping(value="/edit{id}")
    public ModelAndView getEditGuideView(@PathVariable(value = "id") Long guideId){
        var guide = guideService.getGuide(guideId);
        var mav = new ModelAndView();
        mav.addObject("guide", guide);
        mav.addObject("edit",true);
        User user = userService.getUser(auditor.getCurrentAuditor().get().toString()).get();
        if (holder.isHold(guide.get())) {
            mav = getGuidesListView();
            mav.addObject("error", "Edit is not available!");
            var desc = "This entity currently edited by user: " + holder.getHolderOf(guide.get()).getUserName()
                    + ". Try again later or request  to release this entity";
            mav.addObject("errorDesc", desc);

            return mav;
        } else {
            holder.hold(guide.get(), user);
        }
        mav.setViewName("addGuidePage");
        return mav;
    }

    @RequestMapping(value = "/delete{id}")
    public ModelAndView deleteGuide(@PathVariable(value = "id") Long guideId){
        var mav = new ModelAndView();
        var guide = guideService.getGuide(guideId);
        if(!guide.isEmpty() && guide.isPresent()){
            var g = guide.get();
            if (holder.isHold(g)) {
                mav = getGuidesListView();
                mav.addObject("error", "Delete rejected!");
                var desc = "This entity currently edited by user: " + holder.getHolderOf(guide.get()).getUserName()
                        + ". Try again later or request  to release this entity";
                mav.addObject("errorDesc", desc);

                return mav;
            } else {
                if(!guideService.hasFutureTours(g)){
                    g.setDeleted(true);
                    var v = guideService.createOrUpdateGuide(g);
                    logger.info("Deleted guide "+v.getId()+" "+v.getFullName()+" by "+v.getLastModifiedBy());
                }else{
                    mav = getGuidesListView();
                    mav.addObject("error", "Delete rejected!");
                    var desc = "Guide: " + g.getFullName() + " included in tours: ";
                    var futureTours = guideService.getGuideFutureTours(g);
                    for (Tour t : futureTours) {
                        desc += t.getTittle() + ";\n";
                    }
                    desc += "Please exclude guide from tours above and try again.";
                    mav.addObject("errorDesc", desc);
                    return mav;
                }
            }
        }
        mav.addObject("guides", guideService.getGuidesList());
        mav.addObject("activeGuides", true);
        mav.setViewName("guidesListPage");
        return mav;
    }

    // action todo: make single method for adding guide to db
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String addOrUpdateGuideAction(Guide guide) {
        // if exist => edit => hold
        var exist = guideService.exist(guide);
        if(exist)
            holder.release(guide);
        
        var g = guideService.createOrUpdateGuide(guide);
        var uuid = UUID.randomUUID().toString();
        if (exist)
            logger.info(uuid + " edit " + g.toString());
        else
            logger.info(uuid + " create " + g.toString());

        return "redirect:/guides/list";
    }
}
