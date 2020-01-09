package stanislav.tun.novinomad.picasso.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import javax.validation.constraints.Null;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;

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

    // todo : refactor and add possibility to set many drivers during create action
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String addTourAction(@ModelAttribute("tourForm") @Valid Tour tour,
                                @RequestParam(required = false, name = "driverId") Long driverId,
                                Model model) {
        try {
            System.out.printf(mapper.writeValueAsString(tour));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

        var start = tour.getStartDate();
        var end = tour.getEndDate();

        if (start != null && end != null)
            tour.setDays(end.getDayOfYear() - start.getDayOfYear());

        if(driverId != null){
            System.out.println("Driver id in addTourAction param = "+ driverId);
            var driver = driverService.getDriver(driverId);
            System.out.println("DRIVER = "+driver.toString());
            tour.addDriver(driver);
        }else{
            System.out.println("DRIVER ID IS EMPTY!");
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
        var driver = new Driver("pizdabol", "Pizdabolovich");
        driverService.createDriver(driver);
        tour.addDriver(Optional.of(driver));
        tourService.createTour(tour);
    }

    // todo : debug method. Remove for production
    @RequestMapping("/init")
    public String init(){
        // todo: remove saving drivers!
        var d1 = new Driver("Stanislav", "Tun");
        var d2 = new Driver("Alexander", "Baratov");

        driverService.createDriver(d1);
        driverService.createDriver(d2);

        return "redirect:/tours/add";
    }


}
