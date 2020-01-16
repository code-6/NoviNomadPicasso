package stanislav.tun.novinomad.picasso.persistance.pojos;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class MyIntervalTest {

    @Test
    public void overlaps() {
        var i = new MyInterval(LocalDateTime.now(), LocalDateTime.now().plusDays(10));
        var res = i.overlaps(new MyInterval(LocalDateTime.now().plusDays(11), LocalDateTime.now().plusDays(15)));
        assertFalse(res);
    }

    @Test
    public void overlaps2() {
        var i = new MyInterval(LocalDateTime.now(), LocalDateTime.now().plusDays(10));
        var res = i.overlaps(new MyInterval(LocalDateTime.now().plusDays(9), LocalDateTime.now().plusDays(15)));
        assertTrue(res);
    }

    @Test
    public void toDaysList(){
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
}