package stanislav.tun.novinomad.picasso.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stanislav.tun.novinomad.picasso.PicassoApp;
import stanislav.tun.novinomad.picasso.persistance.pojos.MyInterval;

import javax.xml.bind.ValidationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public abstract class IntervalResolver {

    private static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    Logger logger = LoggerFactory.getLogger(PicassoApp.class);

    /**
     * @param dates should contain list of days in format: 01.01.2020;02.01.2020;...dd.mm.yyyy
     * @return sorted List of LocalDate
     */
    public static List<LocalDate> parseDays(String dates) throws ParseException {
        var list = new ArrayList<LocalDate>();
        var arr = dates.split(",");
        for (String date : arr) {
            list.add(toLocalDate(format.parse(date)));
        }
        Collections.sort(list);
        return list;
    }

    /**
     * @param dates Sorted list of LocalDate
     * @return list of MyInterval
     * Method expected sorted list of LocalDate, then it will found all date intervals
     */
    public static List<MyInterval> toIntervals(List<LocalDate> dates) throws ValidationException {
        var list = new ArrayList<MyInterval>();
        int start = 0, end = 0;
        for (int i = 0; i < dates.size(); i++) {
            LocalDate next = null;
            try {
                next = dates.get(i + 1);
            } catch (IndexOutOfBoundsException e) {
                next = dates.get(i);
            }
            if (!dates.get(i).plusDays(1).isEqual(next)) {
                end = i;
                list.add(new MyInterval(dates.get(start), dates.get(end)));
                start = end + 1;
            }
        }
        return list;
    }

    private static LocalDate toLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }


}
