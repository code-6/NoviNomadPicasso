package stanislav.tun.novinomad.picasso.persistance.pojos;

import org.junit.Test;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class MyIntervalTest {

    @Test
    public void overlaps() throws ValidationException {
        var i = new MyInterval(LocalDateTime.now(), LocalDateTime.now().plusDays(10));
        var res = i.overlaps(new MyInterval(LocalDateTime.now().plusDays(11), LocalDateTime.now().plusDays(15)));
        assertFalse(res);
    }

    @Test
    public void overlaps2() throws ValidationException {
        var i = new MyInterval(LocalDateTime.now(), LocalDateTime.now().plusDays(10));
        var res = i.overlaps(new MyInterval(LocalDateTime.now().plusDays(9), LocalDateTime.now().plusDays(15)));
        assertTrue(res);
    }

    @Test
    public void toDaysList() throws ValidationException {
        var d = LocalDateTime.now().plusDays(10);
        d.plusHours(12);
        var i = new MyInterval(LocalDateTime.now(), d);
        var days = i.toDaysList();
        var counter = 0;
        for (LocalDateTime date : days) {
            counter++;
            System.out.println(counter+" "+date.toString());
        }
        System.out.println("Total days = "+i.getTotalIntervalDays());
        assertEquals(11,days.size());

    }

    @Test
    public void parse() throws ValidationException {
        var strIntervals = "2020-01-17T14:50:44--2020-01-20T14:50:44;2020-01-22T14:50:44--2020-01-25T14:50:44";
        var listIntervals = MyInterval.parse(strIntervals);
//        for (MyInterval i : listIntervals) {
//            System.out.println(JsonPrinter.getString(i));
//        }
        System.out.println(listIntervals);
        assertEquals(2, listIntervals.size());
    }

    @Test(expected = ValidationException.class)
    public void testConstructor() throws ValidationException {
        var start = LocalDateTime.now();
        var end = LocalDateTime.now().minusDays(5);
        new MyInterval(start,end);
    }
}