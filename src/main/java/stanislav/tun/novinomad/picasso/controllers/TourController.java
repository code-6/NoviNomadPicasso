package stanislav.tun.novinomad.picasso.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import stanislav.tun.novinomad.picasso.exceptions.OverlapsException;
import stanislav.tun.novinomad.picasso.persistance.pojos.*;
import stanislav.tun.novinomad.picasso.persistance.services.*;
import stanislav.tun.novinomad.picasso.util.JsonPrinter;

import javax.xml.bind.ValidationException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.lang3.exception.ExceptionUtils.*;
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

    @Autowired
    GuideIntervalService guideIntervalService;

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
        logger.debug("/tours/add requested. Returned view with tour "+ JsonPrinter.getString(tour));
        return new ModelAndView("addTourPage", map);
    }

    @RequestMapping("/list")
    public ModelAndView getToursListView(Model model) {
        var mav = new ModelAndView("toursListPage.html");
        mav.addObject("toursList", tourService.getAllTours());
        logger.debug("/tours/list requested.");
        return mav;
    }

    @RequestMapping(value = "/edit{id}")
    public ModelAndView getEditTourView(@PathVariable(value = "id") Long tourId) {
        var tour = tourService.getTour(tourId).get();

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

        try {
            mav.addObject("tourRange", new DateTimeRange(tour.getStartDate(), tour.getEndDate()).toString());
        } catch (NullPointerException e) {
            logger.error(getStackTrace(e));
        } catch (ValidationException e) {
            logger.error(getStackTrace(e));
        }
        mav.setViewName("addTourPage");
        logger.debug("/tours/edit requested. Returned view with tour " + getString(tour));
        return mav;
    }

    private void setDefaultIntervals(Tour tour) {
        try {
            var attachedDrivers = tour.getDrivers();
            var attachedGuides = tour.getGuides();
            if (tour.getStartDate() != null && tour.getEndDate() != null) {
                for (Driver d : attachedDrivers) {
                    var dti = new DriverTourIntervals(tour, new DateTimeRange(tour.getStartDate(), tour.getEndDate()), d);
                    //todo: why it not updates already existing rows in DB?
                    driverIntervalService.createOrUpdateInterval(dti);
                }

                for (Guide g : attachedGuides) {
                    var gti = new GuideTourIntervals(tour, new DateTimeRange(tour.getStartDate(), tour.getEndDate()), g);
                    guideIntervalService.createOrUpdateInterval(gti);
                }
                logger.debug("Set default intervals for tour: "+tour.getId());
            }
        } catch (ValidationException e) {
            logger.error(getStackTrace(e));
        }
    }

    //todo : add default intervals for guides

    // todo ; refactor to big method. change logic of tour creation
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView addTourAction(@ModelAttribute("tour") Tour tour,
                                      @RequestParam(required = false, name = "drivers2attach") List<Long> drivers2attach,
                                      @RequestParam(required = false, name = "drivers2exclude") List<Long> drivers2exclude,
                                      @RequestParam(required = false, name = "guides2attach") List<Long> guides2attach,
                                      @RequestParam(required = false, name = "guides2exclude") List<Long> guides2exclude,
                                      @RequestParam(name = "tourDateTimeRange") String tourDateTimeRange,
                                      @RequestParam(required = false, name = "file") MultipartFile file,
                                      @RequestParam(required = false, name = "adv") boolean adv) {
        try {
            if (tourDateTimeRange != null && tourDateTimeRange != "") {
                var dtr = DateTimeRange.parseSingle(tourDateTimeRange);
                tour.setStartDate(dtr.getStart());
                tour.setEndDate(dtr.getEnd());
            }
        } catch (ValidationException e) {
            logger.error(getStackTrace(e));
        }

        tour.setFileName(file.getOriginalFilename());

        var mav = new ModelAndView();
        // todo : refactor duplicate try-catch blocks
        try {
            attachDrivers(drivers2attach, tour);
        } catch (OverlapsException e) {
            mav = getAddTourView();
            mav.addObject("error", "Tours date range conflict!");
            mav.addObject("errorDesc", e.getMessage());
            mav.addObject("exception", e);
            mav.addObject("tour", tour);
            logger.error(getStackTrace(e));
            return mav;
        }
        excludeDrivers(drivers2exclude, tour);

        try {
            attachGuides(guides2attach, tour);
        } catch (OverlapsException e) {
            mav = getAddTourView();
            mav.addObject("error", "Tours date range conflict!");
            mav.addObject("errorDesc", e.getMessage());
            mav.addObject("exception", e);
            mav.addObject("tour", tour);
            logger.error(getStackTrace(e));
            return mav;
        }
        excludeGuides(guides2exclude, tour);

        tourService.createOrUpdateTour(tour);
        if (adv) {
            if (tour.getStartDate() == null && tour.getEndDate() == null) {
                mav = getAddTourView();
                mav.addObject("error", "Tour date range not chosen!");
                mav.addObject("errorDesc", "Advanced options available only for tours with date range specified.\n Tour was created you can just edit edit now.");
            } else {
                var wrapper = new MapWrapper();
                mav.addObject("wrapper", wrapper);
                mav.setViewName("advancedTourPage");
                getAdvancedPage(tour, wrapper);
            }
        } else { // set to whole tour by default
            setDefaultIntervals(tour);
            var m = tour.getStartDate().getMonth().getValue();
            var y = tour.getStartDate().getYear();
            var r = String.format("redirect:/getview?month=%d&year=%d", m, y);
            mav.setViewName(r);
        }
        mav.addObject("tour", tour);
        return mav;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(new SimpleDateFormat("dd.MM.yyyy"), true, 10));
    }

    private void getAdvancedPage(Tour tour, MapWrapper wrapper) {
        // advanced view is dynamic, it contains list of attached drivers to the tour.
        // also input field opposite each driver, to input days
        var attachedDrivers = tour.getDrivers();
        var attachedGuides = tour.getGuides();
        // this is wrapper to map values entered to input fields to related drivers. Used as a model for view, but not for DB

        // this view is also used for edit attached days, and we shall auto fill input fields, if driver was attached for a spec days
        wrapDrivers(attachedDrivers, tour, wrapper);
        wrapGuides(attachedGuides, tour, wrapper);


    }

    private void wrapDrivers(Set<Driver> attachedDrivers, Tour tour, MapWrapper wrapper) {
        for (Driver d : attachedDrivers) {
            // this object is used for DB representation of specific attached days
            var driverTourIntervals = driverIntervalService.getAllRelatedToTourAndDriver(tour, d);
            var allDays = "";
            for (Iterator<DriverTourIntervals> iterator = driverTourIntervals.iterator(); iterator.hasNext(); ) {
                var driverTourInterval = iterator.next();
                //logger.debug("Fill Driver tour inter" + JsonPrinter.getString(mi));
                try {
                    var intervalDays = driverTourInterval.getInterval().toString();
                    allDays += intervalDays;
                } catch (ValidationException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("all days = " + allDays + " for driver " + d.getFullName());
            // in case of new intervals, value will be empty string
            wrapper.getDriverMap().put(d, allDays);
        }
    }

    private void wrapGuides(Set<Guide> attachedGuides, Tour tour, MapWrapper wrapper) {
        for (Guide guide : attachedGuides) {
            // this object is used for DB representation of specific attached days
            var guideTourIntervals = guideIntervalService.getAllRelatedToTourAndGuide(tour, guide);
            var allDays = "";
            for (Iterator<GuideTourIntervals> iterator = guideTourIntervals.iterator(); iterator.hasNext(); ) {
                var guideTourInterval = iterator.next();
                //logger.debug("Fill Driver tour inter" + JsonPrinter.getString(mi));
                try {
                    var intervalDays = guideTourInterval.getInterval().toString();
                    allDays += intervalDays;
                } catch (ValidationException e) {
                    e.printStackTrace();
                }
            }
            // in case of new intervals, value will be empty string
            wrapper.getGuideMap().put(guide, allDays);
        }
    }

    @PostMapping("advanced/save")
    public ModelAndView advancedSave(@ModelAttribute("wrapper") MapWrapper wrapper,
                                     @RequestParam(name = "tourId") Long tourId) {
        var mav = new ModelAndView();
        mav.addObject("wrapper", wrapper);
        var tour = tourService.getTour(tourId).get();

        saveAdvancedDrivers(wrapper, tour);
        saveAdvancedGuides(wrapper, tour);
        logger.debug("Advanced save tour = " + getString(tour));
        mav.setViewName("toursListPage");
        return mav;
    }

    private void saveAdvancedDrivers(MapWrapper wrapper, Tour tour) {
        var driversMap = wrapper.getDriverMap();
        for (Driver d : driversMap.keySet()) {
            var dates = driversMap.get(d);
            logger.debug("dates + time from view " + dates);
            // todo : fix this hodgie code (updating set didn't helps)
            //var driverTourIntervals = driverIntervalService.getAllRelatedToTourAndDriver(tour, d);
            var setDti = d.getDriverTourIntervals();
            if (setDti.size() > 0) {
                for (DriverTourIntervals dti : setDti) {
                    driverIntervalService.delete(dti);
                    tour.getDriverIntervals().clear();
                }
            }
            try {
                var dateTimeRange = DateTimeRange.parseSingle(dates);
                var dti = new DriverTourIntervals(tour, dateTimeRange, d);
                d.getDriverTourIntervals().add(dti);
                // todo : why not updated already exist intervals? cause above always created new interval. Can be used for create new row, but not for update
                driverIntervalService.createOrUpdateInterval(dti);

            } catch (ValidationException e) {
                e.printStackTrace();
            }

        }
    }

    private void saveAdvancedGuides(MapWrapper wrapper, Tour tour) {
        var guidesMap = wrapper.getGuideMap();
        for (Guide guide : guidesMap.keySet()) {
            var dates = guidesMap.get(guide);
            // todo : fix this hodgie code (updating set didn't helps)
            //var driverTourIntervals = driverIntervalService.getAllRelatedToTourAndDriver(tour, d);
            var setGti = guide.getGuideTourIntervals();
            if (setGti.size() > 0) {
                for (GuideTourIntervals gti : setGti) {
                    guideIntervalService.delete(gti);
                }
                guide.getGuideTourIntervals().clear();
            }
            try {
                var dateTimeRange = DateTimeRange.parseSingle(dates);
                var gti = new GuideTourIntervals(tour, dateTimeRange, guide);
                guide.getGuideTourIntervals().add(gti);
                // todo : why not updated already exist intervals? cause above always created new interval. Can be used for create new row, but not for update
                guideIntervalService.createOrUpdateInterval(gti);

            } catch (ValidationException e) {
                e.printStackTrace();
            }

        }
    }

//    private void saveAdvancedDrivers(MapWrapper wrapper, Tour tour){
//        var driversMap = wrapper.getDriverMap();
//        for (Driver d : driversMap.keySet()) {
//            var dates = driversMap.get(d);
//            // todo : fix this hodgie code (updating set didn't helps)
//            //var driverTourIntervals = driverIntervalService.getAllRelatedToTourAndDriver(tour, d);
//            var setDti = d.getDriverTourIntervals();
//            if (setDti.size() > 0) {
//                for (DriverTourIntervals dti : setDti) {
//                    driverIntervalService.delete(dti);
//                    tour.getDriverIntervals().clear();
//                }
//            }
//            try {
//                var listDates = IntervalResolver.parseDays(dates);
//                var listIntervals = IntervalResolver.toIntervals(listDates);
//                for (DateRange i : listIntervals) {
//                    //logger.debug("INSERT driver tour inter " + JsonPrinter.getString(i));
//                    var dti = new DriverTourIntervals(tour, i, d);
//                    d.getDriverTourIntervals().add(dti);
//                    // todo : why not updated already exist intervals? cause above always created new interval. Can be used for create new row, but not for update
//                    driverIntervalService.createOrUpdateInterval(dti);
//                }
//            } catch (ValidationException e) {
//                e.printStackTrace();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

//    private void saveAdvancedGuides(MapWrapper wrapper, Tour tour) {
//        var guidesMap = wrapper.getGuideMap();
//        for (Guide guide : guidesMap.keySet()) {
//            var dates = guidesMap.get(guide);
//            // todo : fix this hodgie code (updating set didn't helps)
//            //var driverTourIntervals = driverIntervalService.getAllRelatedToTourAndDriver(tour, d);
//            var setGti = guide.getGuideTourIntervals();
//            if (setGti.size() > 0) {
//                for (GuideTourIntervals gti : setGti) {
//                    guideIntervalService.delete(gti);
//                }
//            }
//            try {
//                var listDates = IntervalResolver.parseDays(dates);
//                var listIntervals = IntervalResolver.toIntervals(listDates);
//                for (DateRange i : listIntervals) {
//                    //logger.debug("INSERT driver tour inter " + JsonPrinter.getString(i));
//                    var gti = new GuideTourIntervals(tour, i, guide);
//                    // todo : why not updated already exist intervals? cause above always created new interval. Can be used for create new row, but not for update
//                    guideIntervalService.createOrUpdateInterval(gti);
//                }
//            } catch (ValidationException e) {
//                e.printStackTrace();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

    private void attachDrivers(List<Long> drivers2attach, Tour tour) throws OverlapsException {
        // for edit purposes
        tour.addDriver(tourService.getAttachedDrivers(tour.getId()));
        if (drivers2attach != null) {
            if (drivers2attach.size() > 0)
                for (Long id : drivers2attach) {
                    var driver = driverService.getDriver(id);
                    try {
                        checkAlreadyAppointedDate(driver.get(), tour);
                        tour.addDriver(driver);
                    } catch (NullPointerException e) {
                        // ignored
                    }
                }
        } else {
            // check when edit tour
            for (Driver driver : tour.getDrivers()) {
                checkAlreadyAppointedDate(driver, tour);
            }
        }
    }

    private void attachGuides(List<Long> guides2attach, Tour tour) throws OverlapsException {
        // for edit purposes
        tour.addGuide(tourService.getAttachedGuides(tour.getId()));
        if (guides2attach != null) {
            if (guides2attach.size() > 0)
                for (Long id : guides2attach) {
                    var guide = guideService.getGuide(id);
                    try {
                        checkAlreadyAppointedDate(guide.get(), tour);
                    } catch (NullPointerException e) {
                        // ignored
                    }
                    // todo : before attach driver to the tour, check if driver already attached for this date in another tours
                    tour.addGuide(guide);
                }
        } else {
            for (Guide guide : tour.getGuides()) {
                checkAlreadyAppointedDate(guide, tour);
            }
        }
    }

    private void excludeDrivers(List<Long> drivers2exclude, Tour tour) {
        if (drivers2exclude != null)
            if (drivers2exclude.size() > 0)
                for (Long id : drivers2exclude) {
                    var driver = driverService.getDriver(id);
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

    private void checkAlreadyAppointedDate(AbstractEntity entity, Tour t) throws OverlapsException {

        if (entity instanceof Driver) {
            Driver d = (Driver) entity;
            var allDriverIntervals = driverIntervalService.getAllRelatedToDriver(d);
            for (DriverTourIntervals dti : allDriverIntervals) {
                try {
                    if (dti.getTour().getId() != t.getId())
                        if (dti.getInterval().overlaps(new DateTimeRange(t.getStartDate(), t.getEndDate()))) {
                            throw new OverlapsException(d, t, dti.getTour());
                        }
                } catch (ValidationException e) {
                    e.printStackTrace();
                }
            }
        } else if (entity instanceof Guide) {
            Guide g = (Guide) entity;
            var allGuideIntervals = guideIntervalService.getAllRelatedToGuide(g);
            for (GuideTourIntervals gti : allGuideIntervals) {
                try {
                    if (gti.getTour().getId() != t.getId()) // fix bug when edit tour. Exception thrown even when no overlaps
                        if (gti.getInterval().overlaps(new DateTimeRange(t.getStartDate(), t.getEndDate()))) {
                            throw new OverlapsException(g, t, gti.getTour());
                        }
                } catch (ValidationException e) {
                    // ignore exception
                    logger.error(e.getMessage());
                }
            }
        }
    }


}
