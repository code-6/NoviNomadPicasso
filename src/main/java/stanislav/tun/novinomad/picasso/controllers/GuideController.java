package stanislav.tun.novinomad.picasso.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import stanislav.tun.novinomad.picasso.persistance.pojos.Guide;
import stanislav.tun.novinomad.picasso.persistance.services.GuideService;

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
}
