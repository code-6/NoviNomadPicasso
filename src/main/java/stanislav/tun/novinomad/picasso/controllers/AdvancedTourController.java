package stanislav.tun.novinomad.picasso.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import stanislav.tun.novinomad.picasso.persistance.pojos.MyDayOfWeek;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Calendar;

@Controller
@RequestMapping("/")
public class AdvancedTourController {
    private static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

    @GetMapping("/getview")
    public ModelAndView getView(@RequestParam(required = false, name = "month") Integer month,
                                @RequestParam(required = false, name = "year") String year) {
        if(month == null && year == null){
            month = LocalDate.now().getMonthValue();
            year = String.valueOf(LocalDate.now().getYear());
        }

        var mav = new ModelAndView();
        mav.setViewName("toursCalendarPage");
        System.out.println("parameter on input "+month);
        var ym = YearMonth.of(Integer.valueOf(year), month);
        var totalDays = ym.lengthOfMonth();
        var days = new MyDayOfWeek[totalDays];
        // fill dates
        for (int i = 0; i < totalDays; i++) {
            days[i] = new MyDayOfWeek(LocalDate.of(ym.getYear(), ym.getMonthValue(), i + 1).getDayOfWeek(), i + 1);
        }

        mav.addObject("days", days);
        return mav;
    }

}
