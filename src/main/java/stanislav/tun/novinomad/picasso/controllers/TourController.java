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
import stanislav.tun.novinomad.picasso.persistance.services.GuideService;
import stanislav.tun.novinomad.picasso.persistance.services.TourService;
import stanislav.tun.novinomad.picasso.util.IntervalResolver;

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
    GuideService guideService;

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

        map.put("guides", guideService.getGuidesList());
        map.put("guidesExclude", tour.getGuides());
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
        var allGuides = guideService.getGuidesList();
        // exclude already attached drivers from view
        allDrivers.removeAll(tour.getDrivers());
        // exclude already attached drivers from view
        allGuides.removeAll(tour.getGuides());

        mav.addObject("drivers", allDrivers);
        mav.addObject("driversExclude", tour.getDrivers());

        mav.addObject("guides", allGuides);
        mav.addObject("guidesExclude", tour.getGuides());
        mav.setViewName("addTourPage.html");
        return mav;
    }

    private void setDefaultIntervals(Tour tour, ModelAndView mav){
        try {
            Set<Driver> attachedDrivers = tour.getDrivers();
            if (tour.getStartDate() != null && tour.getEndDate() != null) {
                for (Driver d : attachedDrivers) {
                    var dti = new DriverTourIntervals(tour, new MyInterval(tour.getStartDate(), tour.getEndDate()), d);
                    //todo: why it not updates already existing rows in DB?
                    driverIntervalService.createOrUpdateInterval(dti);
                }
            }
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        mav.setViewName("redirect:/tours/add");
    }

    // todo ; refactor to big method. change logic of tour creation
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView addTourAction(@ModelAttribute("tour") @Valid Tour tour,
                                      @RequestParam(required = false, name = "drivers2attach") List<Long> drivers2attach,
                                      @RequestParam(required = false, name = "drivers2exclude") List<Long> drivers2exclude,
                                      @RequestParam(required = false, name = "guides2attach") List<Long> guides2attach,
                                      @RequestParam(required = false, name = "guides2exclude") List<Long> guides2exclude,
                                      @RequestParam(required = false, name = "adv") boolean adv) {
        var mav = new ModelAndView();
        //setTotalDays(tour);
        attachDrivers(drivers2attach, tour);
        excludeDrivers(drivers2exclude, tour);

        attachGuides(guides2attach, tour);
        excludeGuides(guides2exclude, tour);

        logger.debug("addTourAction TOUR BEFORE INSERT = " + getString(tour));
        tourService.createOrUpdateTour(tour);
        mav.addObject("tour", tour);
        if (adv) {
            getAdvancedPage(tour, mav);
        } else { // set to whole tour by default
            setDefaultIntervals(tour, mav);
        }
        return mav;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(new SimpleDateFormat("dd-MM-yyyy"), true, 10));
    }

    private void getAdvancedPage(Tour tour, ModelAndView mav) {
        // advanced view is dynamic, it contains list of attached drivers to the tour.
        // also input field opposite each driver, to input days
        var attachedDrivers = tour.getDrivers();
        // this is wrapper to map values entered to input fields to related drivers. Used as a model for view, but not for DB
        var wrapper = new MapWrapper();
        // this view is also used for edit attached days, and we shall auto fill input fields, if driver was attached for a spec days
        for (Driver d : attachedDrivers) {
            // this object is used for DB representation of specific attached days
            var driverTourIntervals = driverIntervalService.getAllRelatedToTourAndDriver(tour, d);
            var allDays = "";
            for (Iterator<DriverTourIntervals> iterator = driverTourIntervals.iterator(); iterator.hasNext(); ) {
                var driverTourInterval = iterator.next();
                //logger.debug("Fill Driver tour inter" + JsonPrinter.getString(mi));
                try {
                    var intervalDays = driverTourInterval.getInterval().toDaysStringList();
                    if (iterator.hasNext())
                        allDays += intervalDays + ",";
                    else
                        allDays += intervalDays;
                } catch (ValidationException e) {
                    e.printStackTrace();
                }
            }
            // in case of new intervals, value will be empty string
            wrapper.getDriverMap().put(d, allDays);
        }
        mav.addObject("driversWrapper", wrapper);
        mav.setViewName("advancedTourPage.html");
    }

    @PostMapping("advanced/save")
    public ModelAndView advancedSave(@ModelAttribute("driversWrapper") MapWrapper wrapper,
                                     @RequestParam(name = "tourId") Long tourId) {
        var mav = new ModelAndView();
        mav.addObject("driversWrapper", wrapper);
        var tour = tourService.getTour(tourId).get();
        for (Driver d : wrapper.getDriverMap().keySet()) {
            var dates = wrapper.getDriverMap().get(d);
            // todo : fix this hodgie code (updating set didn't helps)
            //var driverTourIntervals = driverIntervalService.getAllRelatedToTourAndDriver(tour, d);
            var setDti = d.getDriverTourIntervals();
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
                    // todo : why not updated already exist intervals? cause above always created new interval. Can be used for create new row, but not for update
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

        guideService.createOrUpdateGuide(new Guide("Guide1","Guide1"));
        guideService.createOrUpdateGuide(new Guide("Guide2","Guide2"));
        guideService.createOrUpdateGuide(new Guide("Guide3","Guide3"));
        guideService.createOrUpdateGuide(new Guide("Guide4","Guide4"));
        guideService.createOrUpdateGuide(new Guide("Guide5","Guide5"));

        return "redirect:/tours/add";
    }

    private void attachDrivers(List<Long> drivers2attach, Tour tour) {
        // for edit purposes
        tour.addDriver(tourService.getAttachedDrivers(tour.getId()));
        if (drivers2attach != null)
            if (drivers2attach.size() > 0)
                for (Long id : drivers2attach) {
                    var driver = driverService.getDriver(id);
                    // todo : before attach driver to the tour, check if driver already attached for this date in another tours
                    tour.addDriver(driver);
                }
    }

    private void attachGuides(List<Long> guides2attach, Tour tour) {
        // for edit purposes
        tour.addGuide(tourService.getAttachedGuides(tour.getId()));
        if (guides2attach != null)
            if (guides2attach.size() > 0)
                for (Long id : guides2attach) {
                    var guide = guideService.getGuide(id);
                    // todo : before attach driver to the tour, check if driver already attached for this date in another tours
                    tour.addGuide(guide);
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

    private void excludeGuides(List<Long> guides2exclude, Tour tour) {
        if (guides2exclude != null)
            if (guides2exclude.size() > 0)
                for (Long id : guides2exclude) {
                    var guide = guideService.getGuide(id);
//                    if(Validator.overlaps(driver.get(), start, end)){
//                        // todo : show error to user
//                    }
                    tour.deleteGuide(guide.get());
                }
    }
//    // todo : better to put this logic to Tour class in setters and getters
//    private void setTotalDays(Tour tour) {
//        var start = tour.getStartDate();
//        var end = tour.getEndDate();
//
//        if (start != null && end != null) {
//            // todo : check, possible wrong count of days, maybe +1 shall be added
//            tour.setDays(end.getDayOfYear() - start.getDayOfYear());
//        }
//    }
}
