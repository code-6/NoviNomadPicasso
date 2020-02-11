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
import stanislav.tun.novinomad.picasso.persistance.repositories.ITourRepo;
import stanislav.tun.novinomad.picasso.persistance.services.DriverService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;

@Controller
@RequestMapping("/")
public class AdvancedTourController {
    private static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

    @Autowired
    private ITourRepo tourRepo;

    @Autowired
    private DriverService driverService;

    private Logger logger = LoggerFactory.getLogger(AdvancedTourController.class);

    @GetMapping("/getview")
    public ModelAndView getView(@RequestParam(required = false, name = "month") Integer month,
                                @RequestParam(required = false, name = "year") String year) {
        // set default values for month and year. Current date by default
        logger.debug(" call getView() INPUT DATA month = "+month+" year = "+year);
        if(month == null)
            month = LocalDate.now().getMonthValue();

        if(year == null)
            year = String.valueOf(LocalDate.now().getYear());

        var mav = new ModelAndView();
        mav.setViewName("toursCalendarPage");

        var ym = YearMonth.of(Integer.valueOf(year), month);
        var totalDays = ym.lengthOfMonth();
        var days = new LocalDate[totalDays];
        // fill dates
        for (int i = 0; i < totalDays; i++) {
            days[i] = LocalDate.of(Integer.parseInt(year), month, i+1);
            //days[i].getDayOfWeek().name().substring(0,1);
//            days[i] = new MyDayOfWeek(LocalDate.of(ym.getYear(), ym.getMonthValue(), i + 1).getDayOfWeek(), i + 1);
//            days[i].setMonth(month);
        }
        // put days of month with days of week
        mav.addObject("days", days);
        // remember user selected month and year to display after refresh page
        mav.addObject("selectedMonth", num2month(month));
        mav.addObject("selectedYear", year);
        // add tours created for entered month
        var tours = tourRepo.findToursByMonthAndYear(month, Integer.valueOf(year));
        mav.addObject("tours", tours);
        return mav;
    }

    @GetMapping("/getViewRelatedTo{driverId}")
    public ModelAndView getView(@RequestParam(required = false, name = "month") Integer month,
                                         @RequestParam(required = false, name = "year") String year,
                                         @PathVariable(value = "driverId") Long driverId){

        logger.debug(" call second getView() INPUT DATA month = "+month+" year = "+year+" driver id = "+driverId);
        // set default values for month and year. Current date by default
        if(month == null)
            month = LocalDate.now().getMonthValue();

        if(year == null)
            year = String.valueOf(LocalDate.now().getYear());

        var mav = new ModelAndView();
        mav.setViewName("toursCalendarPage");

        var ym = YearMonth.of(Integer.valueOf(year), month);
        var totalDays = ym.lengthOfMonth();
        var days = new LocalDate[totalDays];
        // fill dates
        for (int i = 0; i < totalDays; i++) {
            days[i] = LocalDate.of(Integer.parseInt(year), month, i+1);
            //days[i].getDayOfWeek().name().substring(0,1);
//            days[i] = new MyDayOfWeek(LocalDate.of(ym.getYear(), ym.getMonthValue(), i + 1).getDayOfWeek(), i + 1);
//            days[i].setMonth(month);
        }
        // put days of month with days of week
        mav.addObject("days", days);
        // remember user selected month and year to display after refresh page
        mav.addObject("selectedMonth", num2month(month));
        mav.addObject("selectedYear", year);

        var tours = tourRepo.findToursByMonthAndYear(month, Integer.valueOf(year));
        // get only where driver is specified
//        for (Tour t : tours) {
//            for (Driver d : t.getDrivers()) {
//                if(d.getId() != driverId)
//                    tours.remove(t);
//            }
//        }
        var iterator = tours.iterator();
        while (iterator.hasNext()){
            var tour = iterator.next();
            var i = tour.getDrivers().iterator();
            while(i.hasNext()){
                var driver = i.next();
                if(driver.getId() != driverId)
                    iterator.remove();
            }
        }

        mav.addObject("tours", tours);
        return mav;


    }

    private String num2month(int month){
        switch (month){
            case 1:return "January";
            case 2:return "February";
            case 3:return "March";
            case 4:return "April";
            case 5:return "May";
            case 6:return "June";
            case 7:return "July";
            case 8:return "August";
            case 9:return "September";
            case 10:return "November";
            case 11:return "October";
            case 12:return "December";
        }
        throw new IllegalArgumentException("Can accept only integers in range 1-12");
    }

}
