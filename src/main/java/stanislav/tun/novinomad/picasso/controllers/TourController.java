package stanislav.tun.novinomad.picasso.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;
import stanislav.tun.novinomad.picasso.persistance.services.DriverService;
import stanislav.tun.novinomad.picasso.persistance.services.TourService;
import stanislav.tun.novinomad.picasso.util.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static stanislav.tun.novinomad.picasso.util.JsonPrinter.getString;

@Controller
@RequestMapping("/tours")
public class TourController {
    @Autowired
    TourService tourService;

    @Autowired
    DriverService driverService;

    @Autowired
    ObjectMapper mapper;

    Logger logger = LoggerFactory.getLogger(TourController.class);

    @RequestMapping("/add")
    public ModelAndView getAddTourView(Model model) {
        var map = new HashMap<String, Object>();
        var tour = new Tour();
        map.put("tour", tour);
        // todo : get drivers list from cash data
        map.put("drivers", driverService.getDriversList());
        map.put("driversExclude",tour.getDrivers());
        return new ModelAndView("addTourPage.html", map);
    }

    @RequestMapping("/list")
    public ModelAndView getToursListView(Model model) {
        var mav = new ModelAndView("toursListPage.html");
        mav.addObject("toursList", tourService.getAllTours());
        return mav;
    }

    @RequestMapping(value = "/edit{id}")
    public ModelAndView getEditTourView(@PathVariable(value = "id") Long tourId) {
        var tour = tourService.getTour(tourId);
        logger.debug("getEditTourView TOUR TO EDIT " + getString(tour.get()));
        var mav = new ModelAndView();
        mav.addObject("tour", tour);
        var allDrivers = driverService.getDriversList();
        // exclude already attached drivers from view
        allDrivers.removeAll(tour.get().getDrivers());
        mav.addObject("drivers", allDrivers);
        mav.addObject("driversExclude",tour.get().getDrivers());
        mav.setViewName("addTourPage.html");
        return mav;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String addTourAction(@ModelAttribute("tour") Tour tour,
                                @RequestParam(required = false, name = "drivers2attach") List<Long> drivers2attach,
                                @RequestParam(required = false, name = "drivers2exclude") List<Long> drivers2exclude,
                                Model model) {
        logger.debug("addTourAction TOUR PARAM = " + getString(tour));
        var t = tourService.getTour(tour.getId());
        logger.debug("DEBUG tour optional isEmpty: " + t.isEmpty() + "; isPresent: " + t.isPresent());
        var start = tour.getStartDate();
        var end = tour.getEndDate();

        if (!t.isEmpty() && t.isPresent()) {
            Set<Driver> tDrivers = t.get().getDrivers();
            tour.addDriver(tDrivers);
        }

        if (start != null && end != null) {
            tour.setDays(end.getDayOfYear() - start.getDayOfYear());
        }

        if (drivers2attach != null)
            if (drivers2attach.size() > 0)
                for (Long id : drivers2attach) {
                    var driver = driverService.getDriver(id);
//                    if(Validator.overlaps(driver.get(), start, end)){
//                        // todo : show error to user
//                    }
                    tour.addDriver(driver);
                }
        if (drivers2exclude != null)
            if (drivers2exclude.size() > 0)
                for (Long id : drivers2exclude) {
                    var driver = driverService.getDriver(id);
//                    if(Validator.overlaps(driver.get(), start, end)){
//                        // todo : show error to user
//                    }
                    tour.deleteDriver(driver.get());
                }

        logger.debug("addTourAction TOUR BEFORE INSERT = " + getString(tour));
        tourService.createOrUpdateTour(tour);
        model.addAttribute("tour", tour);
        return "redirect:/tours/add";
    }

    // todo : debug method. Remove for production
    @RequestMapping("/init")
    public String init() {
        var d1 = new Driver("Ryan", "Cooper");
        var d2 = new Driver("Ken", "Miles");
        var d3 = new Driver("Michael", "Schumacher");
        var d4 = new Driver("Ken", "Block");

        driverService.createOrUpdateDriver(d1);
        driverService.createOrUpdateDriver(d2);
        driverService.createOrUpdateDriver(d3);
        driverService.createOrUpdateDriver(d4);
        driverService.createOrUpdateDriver(new Driver("Carroll","Shelby"));

        return "redirect:/tours/add";
    }
}
