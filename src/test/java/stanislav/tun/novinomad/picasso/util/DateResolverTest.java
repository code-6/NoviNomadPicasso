package stanislav.tun.novinomad.picasso.util;

import org.junit.Test;
import stanislav.tun.novinomad.picasso.persistance.pojos.MyDayOfWeek;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.Assert.*;

public class DateResolverTest {

    @Test
    public void getDaysCount() {
        var totalDays = YearMonth.of(2020, 2).lengthOfMonth();
        var myDates = new MyDayOfWeek[totalDays];
        // fill dates
        for (int i = 0; i < totalDays; i++) {
            myDates[i] = new MyDayOfWeek(LocalDate.of(2020, 2, i+1).getDayOfWeek(), i+1);
        }

        // check dates
        for (MyDayOfWeek dw : myDates) {
            System.out.println(dw.toString());
        }
    }
}