package stanislav.tun.novinomad.picasso.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;
import stanislav.tun.novinomad.picasso.persistance.services.DriverService;
import stanislav.tun.novinomad.picasso.persistance.services.TourService;

@Controller
@RequestMapping("/tours")
public class TourController {
    @Autowired
    TourService tourService;
    //todo: make it like a cash data
    @Autowired
    DriverService driverService;

    @RequestMapping("/add")
    public String getAddTourView(Model model){
        Tour tour = new Tour();
        model.addAttribute("tourForm",tour);

        var drivers = driverService.getDriversList();
        model.addAttribute("drivers",drivers);

        return "addTourPage.html";

    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String addDriverAction(@ModelAttribute("tour") Tour tour, Model model) {
        tourService.createTour(tour);
        model.addAttribute("driver", tour);
        return "redirect:/drivers/add";
    }

}
