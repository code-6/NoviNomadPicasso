package stanislav.tun.novinomad.picasso.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import stanislav.tun.novinomad.picasso.persistance.pojos.*;
import stanislav.tun.novinomad.picasso.persistance.services.DriverIntervalService;
import stanislav.tun.novinomad.picasso.persistance.services.DriverService;
import stanislav.tun.novinomad.picasso.persistance.services.TourService;
import stanislav.tun.novinomad.picasso.util.IntervalResolver;
import stanislav.tun.novinomad.picasso.util.JsonPrinter;

import javax.validation.Valid;
import javax.xml.bind.ValidationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    DriverIntervalService driverIntervalService;

    Logger logger = LoggerFactory.getLogger(TourController.class);

    @GetMapping("/add")
    public ModelAndView getAddTourView() {
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
        var tour = tourService.getTour(tourId).get();
        logger.debug("getEditTourView TOUR TO EDIT " + getString(tour));
        var mav = new ModelAndView();
        mav.addObject("tour", tour);
        var allDrivers = driverService.getDriversList();
        // exclude already attached drivers from view
        allDrivers.removeAll(tour.getDrivers());
        mav.addObject("drivers", allDrivers);
        mav.addObject("driversExclude", tour.getDrivers());
        mav.setViewName("addTourPage.html");
        return mav;
    }

    @RequestMapping(value = "/advanced")
    public ModelAndView getAdvancedPage() {
        var mav = new ModelAndView();
        mav.setViewName("advancedTourPage.html");
        var wrapper = new DriverMapWrapper();
        mav.addObject("driversWrapper", wrapper);
        // fill if edit and new if create
        // what is needed for fill model?
        return mav;
    }

    // todo ; refactor to big method. change logic of tour creation
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView addTourAction(@ModelAttribute("tour") @Valid Tour tour,
                                      @RequestParam(required = false, name = "drivers2attach") List<Long> drivers2attach,
                                      @RequestParam(required = false, name = "drivers2exclude") List<Long> drivers2exclude,
                                      @RequestParam(required = false, name = "adv") boolean adv) {
        var mav = new ModelAndView();
        setTotalDays(tour);
        attachDrivers(drivers2attach, tour);
        excludeDrivers(drivers2exclude, tour);

        logger.debug("addTourAction TOUR BEFORE INSERT = " + getString(tour));
        tourService.createOrUpdateTour(tour);
        var attachedDrivers = tour.getDrivers();
        mav.addObject("tour", tour);
        // todo : put out of this method to getAdvancedPage()
        if (adv) {
            var wrapper = new DriverMapWrapper();
            for (Driver d : attachedDrivers) {
                var dti = driverIntervalService.getAllRelatedToTourAndDriver(tour, d);
                var value = "";
                for (Iterator<DriverTourIntervals> iterator = dti.iterator(); iterator.hasNext(); ) {
                    var mi = iterator.next();
                    //logger.debug("Fill Driver tour inter" + JsonPrinter.getString(mi));
                    try {
                        var v = mi.getInterval().toDaysStringList();
                        if (iterator.hasNext())
                            value += v + ",";
                         else
                            value += v;
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    }
                }
                wrapper.getMap().put(d, value);
            }
            mav.addObject("driversWrapper", wrapper);
            mav.setViewName("advancedTourPage.html");
        } else { // set to whole tour by default
            try {
                if (tour.getStartDate() != null && tour.getEndDate() != null) {
                    for (Driver d : attachedDrivers) {
                        var dti = new DriverTourIntervals(tour, new MyInterval(tour.getStartDate(), tour.getEndDate()), d);
                        driverIntervalService.createOrUpdateInterval(dti);
                    }
                }
            } catch (ValidationException e) {
                e.printStackTrace();
            }
            mav.setViewName("redirect:/tours/add");
        }

        return mav;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(new SimpleDateFormat("dd-MM-yyyy"), true, 10));
    }

    @PostMapping("advanced/save")
    public ModelAndView advancedSave(@ModelAttribute("driversWrapper") DriverMapWrapper wrapper,
                                     @RequestParam(name = "tourId") Long tourId) {
        var mav = new ModelAndView();
        mav.addObject("driversWrapper", wrapper);
        var tour = tourService.getTour(tourId).get();
        for (Driver d : wrapper.getMap().keySet()) {
            var dates = wrapper.getMap().get(d);
            // todo : fix this hodgie code (updating set didn't helps)
            //var driverTourIntervals = driverIntervalService.getAllRelatedToTourAndDriver(tour, d);
            var setDti = d.getIntervals();
            if (setDti.size() > 0) {
                for (DriverTourIntervals dti : setDti) {
                    driverIntervalService.delete(dti);
                }
            }
            try {
                var listDates = IntervalResolver.parseDays(dates);
                var listIntervals = IntervalResolver.toIntervals(listDates);
                for (MyInterval i : listIntervals) {
                    //logger.debug("INSERT driver tour inter " + JsonPrinter.getString(i));
                    var dti = new DriverTourIntervals(tour, i, d);
                    driverIntervalService.createOrUpdateInterval(dti);
                }
            } catch (ValidationException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        mav.setViewName("redirect:/tours/list");
        return mav;
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
        driverService.createOrUpdateDriver(new Driver("Carroll", "Shelby"));

        return "redirect:/tours/add";
    }

    private void attachDrivers(List<Long> drivers2attach, Tour tour) {
        // for edit purposes
        tour.addDriver(tourService.getAttachedDrivers(tour.getId()));
        if (drivers2attach != null)
            if (drivers2attach.size() > 0)
                for (Long id : drivers2attach) {
                    var driver = driverService.getDriver(id);
//                    if(Validator.overlaps(driver.get(), start, end)){
//                        // todo : show error to user
//                    }
                    tour.addDriver(driver);
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

    private void setTotalDays(Tour tour) {
        var start = tour.getStartDate();
        var end = tour.getEndDate();

        if (start != null && end != null) {
            tour.setDays(end.getDayOfYear() - start.getDayOfYear());
        }
    }
}
