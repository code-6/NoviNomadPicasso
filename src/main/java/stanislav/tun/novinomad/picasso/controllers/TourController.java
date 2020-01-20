package stanislav.tun.novinomad.picasso.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import stanislav.tun.novinomad.picasso.persistance.pojos.DriverTourIntervals;
import stanislav.tun.novinomad.picasso.persistance.pojos.MyInterval;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;
import stanislav.tun.novinomad.picasso.persistance.services.DriverIntervalService;
import stanislav.tun.novinomad.picasso.persistance.services.DriverService;
import stanislav.tun.novinomad.picasso.persistance.services.TourService;

import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    DriverIntervalService driverIntervalService;

    Logger logger = LoggerFactory.getLogger(TourController.class);

    @RequestMapping("/add")
    public ModelAndView getAddTourView(Model model) {
        var map = new HashMap<String, Object>();
        var tour = new Tour();
        map.put("tour", tour);
        // todo : get drivers list from cash data
        map.put("drivers", driverService.getDriversList());
        map.put("driversExclude", tour.getDrivers());
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
        mav.addObject("driversExclude", tour.get().getDrivers());
        mav.setViewName("addTourPage.html");
        return mav;
    }
    @RequestMapping(value = "/advanced")
    public String getAdvancedPage(){
        return "advancedTourPage.html";
    }

    // todo ; refactor to big method. change logic of tour creation
    @RequestMapping(value = "/save{adv}", method = RequestMethod.POST)
    public String addTourAction(@ModelAttribute("tour") Tour tour,
                                @RequestParam(required = false, name = "drivers2attach") List<Long> drivers2attach,
                                @RequestParam(required = false, name = "drivers2exclude") List<Long> drivers2exclude,
                                @PathVariable(value = "adv") String adv) {
        var t = tourService.getTour(tour.getId());
        logger.debug("DEBUG tour optional isEmpty: " + t.isEmpty() + "; isPresent: " + t.isPresent());

        if (!t.isEmpty() && t.isPresent()) {
            Set<Driver> tDrivers = t.get().getDrivers();
            t.get().addDriver(tDrivers);
        }

        setTotalDays(tour);

        attachDrivers(drivers2attach, tour);

        excludeDrivers(drivers2exclude, tour);

        logger.debug("addTourAction TOUR BEFORE INSERT = " + getString(tour));
        tourService.createOrUpdateTour(tour);
        //model.addAttribute("tour", tour);
        System.out.println("ADV = "+adv);
        if(Boolean.getBoolean(adv)){

            return "redirect:/tours/advanced";
        }

        return "redirect:/tours/add";
    }

//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    public String addTourAction(@ModelAttribute("tour") Tour tour,
//                                @RequestParam(required = false, name = "drivers2attach") Map<Long, String> drivers2attach,
//                                @RequestParam(required = false, name = "drivers2exclude") List<Long> drivers2exclude,
//                                Model model) {
//        tour = getTourDriversForEdit(tour.getId());
//
//        setTotalDays(tour);
//
//        attachDrivers(drivers2attach, tour);
//
//        excludeDrivers(drivers2exclude, tour);
//
//        logger.debug("addTourAction TOUR BEFORE INSERT = " + getString(tour));
//        tourService.createOrUpdateTour(tour);
//        model.addAttribute("tour", tour);
//        return "redirect:/tours/add";
//    }

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
        driverService.createOrUpdateDriver(new Driver("Carroll", "Shelby"));

        return "redirect:/tours/add";
    }

    private void attachDrivers(List<Long> drivers2attach, Tour tour) {
        if (drivers2attach != null)
            if (drivers2attach.size() > 0)
                for (Long id : drivers2attach) {
                    var driver = driverService.getDriver(id);
//                    if(Validator.overlaps(driver.get(), start, end)){
//                        // todo : show error to user
//                    }
                    tour.addDriver(driver);
                    // set to full tour time by default
                    //addDriverIntervals(tour, driver.get(), tour.getStartDate(), tour.getEndDate());
                }
    }

    private void attachDrivers(Map<Long, String> drivers2attach, Tour tour) {
        if (drivers2attach != null)
            if (drivers2attach.size() > 0)
                for (Long id : drivers2attach.keySet()) {
                    var driver = driverService.getDriver(id);
//                    if(Validator.overlaps(driver.get(), start, end)){
//                        // todo : show error to user
//                    }
                    tour.addDriver(driver);
                    // set to full tour time by default
                    addDriverIntervals(tour, driver.get(), drivers2attach.get(id));
                }
    }

    private void excludeDrivers(List<Long> drivers2exclude, Tour tour) {
        if (drivers2exclude != null)
            if (drivers2exclude.size() > 0)
                for (Long id : drivers2exclude) {
                    var driver = driverService.getDriver(id);
//                    if(Validator.overlaps(driver.get(), start, end)){
//                        // todo : show error to user
//                    }
                    tour.deleteDriver(driver.get());
                }
    }

    private void excludeDrivers(Map<Long, String> drivers2exclude, Tour tour) {
        if (drivers2exclude != null)
            if (drivers2exclude.size() > 0)
                for (Long id : drivers2exclude.keySet()) {
                    var driver = driverService.getDriver(id);
//                    if(Validator.overlaps(driver.get(), start, end)){
//                        // todo : show error to user
//                    }
                    tour.deleteDriver(driver.get());
                }
    }

    private void setTotalDays(Tour tour) {
        var start = tour.getStartDate();
        var end = tour.getEndDate();

        if (start != null && end != null) {
            tour.setDays(end.getDayOfYear() - start.getDayOfYear());
        }
    }

//    private Tour getTourDriversForEdit(long tourId) {
//
//        return t.get();
//    }

    public void addDriverIntervals(Tour tour, Driver driver, LocalDateTime from, LocalDateTime to) {
        try {
            var i = new MyInterval(from, to);
            var driverInterval = new DriverTourIntervals(tour, i, driver);
            driverIntervalService.createOrUpdateInterval(driverInterval);
        } catch (ValidationException e) {
            // todo : display error to user
            e.printStackTrace();
        }
    }

    public void addDriverIntervals(Tour tour, Driver driver, String intervals) {
        try {
            var list = MyInterval.parse(intervals);
            for (MyInterval i : list) {
                var driverInterval = new DriverTourIntervals(tour, i, driver);
                driverIntervalService.createOrUpdateInterval(driverInterval);
            }

        } catch (ValidationException e) {
            // todo : display error to user
            e.printStackTrace();
        }
    }
}
