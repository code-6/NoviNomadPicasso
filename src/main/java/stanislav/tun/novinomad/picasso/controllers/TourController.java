package stanislav.tun.novinomad.picasso.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;
import stanislav.tun.novinomad.picasso.persistance.services.DriverService;
import stanislav.tun.novinomad.picasso.persistance.services.TourService;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.HashMap;

@Controller
@RequestMapping("/tours")
public class TourController {
    @Autowired
    TourService tourService;

    @Autowired
    DriverService driverService;

    @Autowired
    ObjectMapper mapper;

    @RequestMapping("/add")
    public ModelAndView getAddTourView(Model model) {
        var map = new HashMap<String, Object>();

        map.put("tourForm", new Tour());
        map.put("drivers", driverService.getDriversList());

        return new ModelAndView("addTourPage.html", map);
    }

    @RequestMapping("/list")
    public ModelAndView getToursListView(Model model){
        var mav = new ModelAndView("toursListPage.html");
        mav.addObject("toursList", tourService.getAllTours());
        return mav;
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String addTourAction(@ModelAttribute("tourForm") @Valid Tour tour, Model model) {

        try {
            System.out.printf(mapper.writeValueAsString(tour));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        var start = tour.getStartDate();
        var end = tour.getEndDate();

        if(start != null && end != null)
            tour.setDays(end.get(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR));

        tourService.createTour(tour);
        model.addAttribute("driver", tour);
        return "redirect:/tours/add";
    }

}
