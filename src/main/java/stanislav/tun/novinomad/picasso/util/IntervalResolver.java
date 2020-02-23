package stanislav.tun.novinomad.picasso.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stanislav.tun.novinomad.picasso.PicassoApp;
import stanislav.tun.novinomad.picasso.persistance.pojos.DateRange;
import stanislav.tun.novinomad.picasso.persistance.pojos.DateTimeRange;

import javax.xml.bind.ValidationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public abstract class IntervalResolver {

    private static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    private static SimpleDateFormat format2 = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    Logger logger = LoggerFactory.getLogger(IntervalResolver.class);

    /**
     * @param dates should contain list of days in format: 01.01.2020;02.01.2020;...dd.mm.yyyy
     * @return sorted List of LocalDate
     */
    public static List<LocalDateTime> parseDays(String dates) throws ParseException {
        var list = new ArrayList<LocalDateTime>();
        var arr = dates.split("-");
        for (String date : arr) {
            list.add(toLocalDateTime(format2.parse(date)));
        }
        Collections.sort(list);
        return list;
    }

    /**
     * @param dates Sorted list of LocalDate
     * @return list of MyInterval
     * Method expected sorted list of LocalDate, then it will found all date intervals
     */
    public static List<DateTimeRange> toIntervals(List<LocalDateTime> dates) throws ValidationException {
        var list = new ArrayList<DateTimeRange>();
        int start = 0, end = 0;
        for (int i = 0; i < dates.size(); i++) {
            LocalDateTime next = null;
            try {
                next = dates.get(i + 1);
            } catch (IndexOutOfBoundsException e) {
                next = dates.get(i);
            }
            if (!dates.get(i).plusDays(1).isEqual(next)) {
                end = i;
                list.add(new DateTimeRange(dates.get(start), dates.get(end)));
                start = end + 1;
            }
        }
        return list;
    }

    public static LocalDate toLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
    public static LocalDateTime toLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }



}
