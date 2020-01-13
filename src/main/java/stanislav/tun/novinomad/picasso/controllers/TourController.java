package stanislav.tun.novinomad.picasso.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;
import stanislav.tun.novinomad.picasso.persistance.services.DriverService;
import stanislav.tun.novinomad.picasso.persistance.services.TourService;

import javax.validation.Valid;
import java.util.*;

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
        System.out.println("getEditTourView TOUR TO EDIT "+getString(tour.get()));
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
        System.out.println("addTourAction TOUR PARAM = "+getString(tour));
        var t = tourService.getTour(tour.getId());
        System.out.println("DEBUG tour optional isEmpty: "+t.isEmpty()+"; isPresent: "+t.isPresent());
        var start = tour.getStartDate();
        var end = tour.getEndDate();

        if (start != null && end != null)
            tour.setDays(end.getDayOfYear() - start.getDayOfYear());

        if(!t.isEmpty() && t.isPresent()){
            Set<Driver> tDrivers = t.get().getDrivers();
            tour.addDriver(tDrivers);
        }

        if(driverId != null)
            if( driverId.size() > 0)
                for(Long id : driverId){
                    var driver = driverService.getDriver(id);
                    tour.addDriver(driver);
                }

        System.out.println("addTourAction TOUR BEFORE INSERT = "+getString(tour));
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
