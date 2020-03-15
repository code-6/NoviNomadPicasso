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
import stanislav.tun.novinomad.picasso.persistance.pojos.User;
import stanislav.tun.novinomad.picasso.persistance.services.GuideService;
import stanislav.tun.novinomad.picasso.persistance.services.UserService;
import stanislav.tun.novinomad.picasso.util.ConcurrentHolder;


@Controller
@RequestMapping("/guides")
public class GuideController {
    @Autowired
    GuideService guideService;

    @Autowired
    ConcurrentHolder holder;

    @Autowired
    AuditorAware auditor;

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(GuideController.class);

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
                g.setDeleted(true);
                guideService.createOrUpdateGuide(g);
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
        if(guideService.exist(guide))
            holder.release(guide);
        
        guideService.createOrUpdateGuide(guide);
        return "redirect:/guides/list";
    }
}
