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

        map.put("tour", new Tour());
        // todo : get drivers list from cash data
        map.put("drivers", driverService.getDriversList());

        return new ModelAndView("addTourPage.html", map);
    }

    @RequestMapping("/list")
    public ModelAndView getToursListView(Model model) {
        var mav = new ModelAndView("toursListPage.html");
        mav.addObject("toursList", tourService.getAllTours());
        return mav;
    }

    @RequestMapping(value="/edit{id}")
    public ModelAndView getEditTourView(@PathVariable(value = "id") Long tourId){
        var tour = tourService.getTour(tourId);
        logger.debug("getEditTourView TOUR TO EDIT "+getString(tour.get()));
        var mav = new ModelAndView();
        mav.addObject("tour", tour);
        var allDrivers = driverService.getDriversList();
        // exclude already attached drivers
        allDrivers.removeAll(tour.get().getDrivers());
        mav.addObject("drivers", allDrivers);
        mav.setViewName("addTourPage.html");
        return mav;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String addTourAction(@ModelAttribute("tour") Tour tour,
                                @RequestParam(required = false, name = "driverId") List<Long> driverId,
                                Model model) {
       logger.debug("addTourAction TOUR PARAM = "+getString(tour));
        var t = tourService.getTour(tour.getId());
       logger.debug("DEBUG tour optional isEmpty: "+t.isEmpty()+"; isPresent: "+t.isPresent());
        var start = tour.getStartDate();
        var end = tour.getEndDate();

        if(!t.isEmpty() && t.isPresent()){
            Set<Driver> tDrivers = t.get().getDrivers();
            tour.addDriver(tDrivers);
        }

        if (start != null && end != null){
            tour.setDays(end.getDayOfYear() - start.getDayOfYear());
        }

        if(driverId != null)
            if( driverId.size() > 0)
                for(Long id : driverId){
                    var driver = driverService.getDriver(id);
                    if(Validator.overlaps(driver.get(), start, end)){
                        // todo : show error to user
                    }
                    tour.addDriver(driver);
                }

       logger.debug("addTourAction TOUR BEFORE INSERT = "+getString(tour));
        tourService.createOrUpdateTour(tour);
        model.addAttribute("tour", tour);
        return "redirect:/tours/add";
    }

    // todo : debug method. Remove for production
    @RequestMapping("/test")
    public void createtour(){
        var tour = new Tour();
        tour.setTittle("Uahahaha");
        var driver = new Driver("asdasd", "asdasf");
        driverService.createOrUpdateDriver(driver);
        tour.addDriver(Optional.of(driver));
        tourService.createOrUpdateTour(tour);
    }

    // todo : debug method. Remove for production
    @RequestMapping("/init")
    public String init(){
        // todo: remove saving drivers!
        var d1 = new Driver("Ryan", "Cooper");
        var d2 = new Driver("Carol", "Shelby");
        var d3 = new Driver("Michael","Schumacher");
        var d4 = new Driver("Ken","Block");

        driverService.createOrUpdateDriver(d1);
        driverService.createOrUpdateDriver(d2);
        driverService.createOrUpdateDriver(d3);
        driverService.createOrUpdateDriver(d4);

        return "redirect:/tours/add";
    }


}
