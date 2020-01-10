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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

        map.put("tourForm", new Tour());
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
        System.out.println("Tour id to be edited = "+tourId);
        var tour = tourService.getTour(tourId);
        System.out.println("Tour to be edited "+tourId.toString());

        var mav = new ModelAndView();
        mav.addObject("tourForm", tour);
        var allDrivers = driverService.getDriversList();
        allDrivers.removeAll(tour.get().getDrivers());
        mav.addObject("drivers", allDrivers);
        mav.setViewName("addTourPage.html");
        return mav;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String addTourAction(@ModelAttribute("tourForm") @Valid Tour tour,
                                @RequestParam(required = false, name = "driverId") List<Long> driverId,
                                Model model) {

        var start = tour.getStartDate();
        var end = tour.getEndDate();

        if (start != null && end != null)
            tour.setDays(end.getDayOfYear() - start.getDayOfYear());

        if(driverId != null){
            if( driverId.size() > 0){
                System.out.println("DriverS more than 1");
                for(Long id : driverId){
                    System.out.println("Driver id in addTourAction param = "+ id);
                    var driver = driverService.getDriver(id);
                    System.out.println(getString(driver.get()));
                    tour.addDriver(driver);
                }
            }
        }
        tourService.createTour(tour);
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
        tourService.createTour(tour);
    }

    // todo : debug method. Remove for production
    @RequestMapping("/init")
    public String init(){
        // todo: remove saving drivers!
        var d1 = new Driver("Stanislav", "Tun");
        var d2 = new Driver("Alexander", "Baratov");

        driverService.createOrUpdateDriver(d1);
        driverService.createOrUpdateDriver(d2);

        return "redirect:/tours/add";
    }


}
