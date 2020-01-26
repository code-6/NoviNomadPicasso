package stanislav.tun.novinomad.picasso.util;

import org.junit.Before;
import org.junit.Test;
import stanislav.tun.novinomad.picasso.persistance.pojos.MyInterval;

import javax.xml.bind.ValidationException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

public class IntervalResolverTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void parseDays() throws ParseException {
        String dates = "02.01.2020; 01.01.2020; 09.01.2020; 04.01.2020; 08.01.2020";
        var list = IntervalResolver.parseDays(dates);
        assertEquals(5,list.size());
        assertEquals(LocalDate.parse("01.01.2020", DateTimeFormatter.ofPattern("dd.MM.yyyy")), list.get(0));
        for (LocalDate d : list) {
            System.out.print(d+"; ");
        }
    }

    @Test
    public void toIntervals() throws ParseException, ValidationException {
        String dates = "02.01.2020; 01.01.2020; 09.01.2020; 04.01.2020; 08.01.2020";
        var list = IntervalResolver.toIntervals(IntervalResolver.parseDays(dates));
        assertEquals(3, list.size());
        for (MyInterval i : list) {
            System.out.println(i.toString());
        }
    }
}