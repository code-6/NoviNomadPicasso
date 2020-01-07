package stanislav.tun.novinomad.picasso.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
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
import java.util.List;

@Controller
@RequestMapping("/tours")
public class TourController {
    @Autowired
    TourService tourService;

    @Autowired
    private DriverService driverService;

    @Autowired
    ObjectMapper mapper;

    Driver d1 = new Driver("Stas", "Wong");
    Driver d2 = new Driver("Roman", "Frik");
    Driver d3 = new Driver("Tolik", "Rakh");

    @RequestMapping("/add")
    public ModelAndView getAddTourView(Model model) {
        var map = new HashMap<String, Object>();

        map.put("tourForm", new Tour());
        driverService.createDriver(d1);
        driverService.createDriver(d2);
        driverService.createDriver(d3);
        map.put("drivers", driverService.getDriversList());

        return new ModelAndView("addTourPage.html", map);
    }

    @RequestMapping("/list")
    public ModelAndView getToursListView(Model model) {
        var mav = new ModelAndView("toursListPage.html");
        mav.addObject("toursList", tourService.getAllTours());
        return mav;
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String addTourAction(@ModelAttribute("tourForm") @Valid Tour tour, @ModelAttribute("drivers") List<Driver> drivers, Model model) {

        try {
            System.out.printf(mapper.writeValueAsString(tour));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        var start = tour.getStartDate();
        var end = tour.getEndDate();

        if (start != null && end != null)
            tour.setDays(end.get(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR));

        if (!drivers.isEmpty())
            for (Driver d : drivers) {
                System.out.println("UAHAHAHAH " + d.toString());
                tour.addDriver(d);
            }

        tourService.createTour(tour);
        model.addAttribute("driver", tour);
        return "redirect:/tours/add";
    }

}
