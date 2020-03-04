package stanislav.tun.novinomad.picasso.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import stanislav.tun.novinomad.picasso.persistance.pojos.Guide;
import stanislav.tun.novinomad.picasso.persistance.services.GuideService;
import stanislav.tun.novinomad.picasso.util.JsonPrinter;

@Controller
@RequestMapping("/guides")
public class GuideController {
    @Autowired
    GuideService guideService;

    Logger logger = LoggerFactory.getLogger(GuideController.class);

    @RequestMapping("/list")
    public ModelAndView getGuidesListView() {
        var guides = guideService.getGuidesList();
        var modelAndView = new ModelAndView();
        modelAndView.addObject("guides", guides);
        modelAndView.setViewName("guidesListPage.html");
        modelAndView.addObject("activeGuides", true);
        return modelAndView;
    }

    // get view
    @RequestMapping("/add")
    public ModelAndView addGuideView() {
        var guide = new Guide();
        var modelAndView = new ModelAndView();
        modelAndView.addObject("guide", guide);
        modelAndView.setViewName("addGuidePage.html");
        return modelAndView;
    }

    @RequestMapping(value="/edit{id}")
    public ModelAndView getEditGuideView(@PathVariable(value = "id") Long guideId){
        var guide = guideService.getGuide(guideId);
        var mav = new ModelAndView();
        mav.addObject("guide", guide);
        mav.setViewName("addGuidePage.html");
        return mav;
    }

    @RequestMapping(value = "/delete{id}")
    public String deleteGuide(@PathVariable(value = "id") Long guideId){
        var guide = guideService.getGuide(guideId);
        if(!guide.isEmpty() && guide.isPresent()){
            var g = guide.get();
            g.setDeleted(true);
            guideService.createOrUpdateGuide(g);
        }

        return "redirect:/guides/list";
    }

    // action todo: make single method for adding guide to db
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String addOrUpdateGuideAction(Guide guide) {
        guideService.createOrUpdateGuide(guide);
        return "redirect:/guides/list";
    }
}
