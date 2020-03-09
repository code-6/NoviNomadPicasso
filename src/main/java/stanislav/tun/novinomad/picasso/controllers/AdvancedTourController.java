package stanislav.tun.novinomad.picasso.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import stanislav.tun.novinomad.picasso.persistance.pojos.YearHolder;
import stanislav.tun.novinomad.picasso.persistance.repositories.ITourRepo;
import stanislav.tun.novinomad.picasso.persistance.services.DriverService;
import stanislav.tun.novinomad.picasso.persistance.services.GuideService;
import stanislav.tun.novinomad.picasso.persistance.services.TourService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;

@Controller
@RequestMapping("/picasso")
public class AdvancedTourController {
    private static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

    @Autowired
    private TourService tourService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private GuideService guideService;

    private Logger logger = LoggerFactory.getLogger(AdvancedTourController.class);

    @GetMapping("/getview")
    public ModelAndView getView(@RequestParam(required = false, name = "month") Integer month,
                                @RequestParam(required = false, name = "year") String year) {
        // set default values for month and year. Current date by default
        if (month == null)
            month = LocalDate.now().getMonthValue();

        if (year == null)
            year = String.valueOf(LocalDate.now().getYear());

        if(YearHolder.yearHolder == 0)
            YearHolder.yearHolder = Integer.valueOf(year);

        var mav = new ModelAndView();
        mav.setViewName("toursCalendarPage");

        var ym = YearMonth.of(Integer.valueOf(YearHolder.yearHolder), month);
        var totalDays = ym.lengthOfMonth();
        var days = new LocalDate[totalDays];
        // fill dates
        for (int i = 0; i < totalDays; i++) {
            days[i] = LocalDate.of(YearHolder.yearHolder, month, i + 1);
        }
        // put days of month with days of week
        mav.addObject("days", days);
        // remember user selected month and year to display after refresh page
        mav.addObject("selectedMonth", num2month(month));
        mav.addObject("selectedMonthNum", month);
        mav.addObject("selectedYear", YearHolder.yearHolder);
        // add tours created for entered month
        var tours = tourService.getToursForDate(month, YearHolder.yearHolder);
        mav.addObject("tours", tours);
        mav.addObject("activePicasso", true);
        return mav;
    }

    @GetMapping("/getViewRelatedToDriver{driverId}")
    public ModelAndView getView(@RequestParam(required = false, name = "month") Integer month,
                                @RequestParam(required = false, name = "year") String year,
                                @PathVariable(value = "driverId") Long driverId) {
        // set default values for month and year. Current date by default
        if (month == null)
            month = LocalDate.now().getMonthValue();

        if (year == null)
            year = String.valueOf(LocalDate.now().getYear());

        var mav = new ModelAndView();
        mav.setViewName("toursCalendarPage");

        var ym = YearMonth.of(Integer.valueOf(year), month);
        var totalDays = ym.lengthOfMonth();
        var days = new LocalDate[totalDays];
        // fill dates
        for (int i = 0; i < totalDays; i++) {
            days[i] = LocalDate.of(Integer.parseInt(year), month, i + 1);
        }
        // put days of month with days of week
        mav.addObject("days", days);
        // remember user selected month and year to display after refresh page
        mav.addObject("selectedMonth", num2month(month));
        mav.addObject("selectedMonthNum", month);
        mav.addObject("selectedYear", year);
        // remember user selected driver
        mav.addObject("selectedDriver", driverService.getDriver(driverId).get());
//        var tours = tourService.getToursForDate(month, Integer.valueOf(year));
//        // get only where driver is specified
//        var ti = tours.iterator();
//        while (ti.hasNext()) {
//            var tour = ti.next();
//            var di = tour.getDrivers().iterator();
//            // if empty driver also delete tour
//            if (!di.hasNext())
//                ti.remove();
//            while (di.hasNext()) {
//                var driver = di.next();
//                if (driver.getId() != driverId)
//                    // illegal state exception was thrown here!
//                    ti.remove();
//            }
//        }
        var tours = tourService.getToursRelated2Driver(driverId, month, Integer.valueOf(year));
        mav.addObject("tours", tours);
        mav.addObject("activePicasso", true);
        return mav;
    }

    @GetMapping("/getViewRelatedToGuide{guideId}")
    public ModelAndView getView2(@RequestParam(required = false, name = "month") Integer month,
                                 @RequestParam(required = false, name = "year") String year,
                                 @PathVariable(value = "guideId") Long guideId) {
        logger.debug(" call third getView2() INPUT DATA month = " + month + " year = " + year + " guide id = " + guideId);
        // set default values for month and year. Current date by default
        if (month == null)
            month = LocalDate.now().getMonthValue();

        if (year == null)
            year = String.valueOf(LocalDate.now().getYear());

        var mav = new ModelAndView();
        mav.setViewName("toursCalendarPage");

        var ym = YearMonth.of(Integer.valueOf(year), month);
        var totalDays = ym.lengthOfMonth();
        var days = new LocalDate[totalDays];
        // fill dates
        for (int i = 0; i < totalDays; i++) {
            days[i] = LocalDate.of(Integer.parseInt(year), month, i + 1);
        }
        // put days of month with days of week
        mav.addObject("days", days);
        // remember user selected month and year to display after refresh page
        mav.addObject("selectedMonth", num2month(month));
        mav.addObject("selectedMonthNum", month);
        mav.addObject("selectedYear", year);
        mav.addObject("selectedGuide", guideService.getGuide(guideId).get());
//        var tours = tourService.getToursForDate(month, Integer.valueOf(year));
//        // get only where driver is specified
//        var ti = tours.iterator();
//        while (ti.hasNext()) {
//            var tour = ti.next();
//            var gi = tour.getGuides().iterator();
//            if (!gi.hasNext())
//                ti.remove();
//            while (gi.hasNext()) {
//                var guide = gi.next();
//                long id = guide.getId();
//                if (id != guideId)
//                    ti.remove();
//            }
//        }
        var tours = tourService.getToursRelated2Guide(guideId, month, Integer.valueOf(year));
        mav.addObject("tours", tours);
        mav.addObject("activePicasso", true);
        return mav;
    }


    private String num2month(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "November";
            case 11:
                return "October";
            case 12:
                return "December";
        }
        throw new IllegalArgumentException("Can accept only integers in range 1-12");
    }

}
