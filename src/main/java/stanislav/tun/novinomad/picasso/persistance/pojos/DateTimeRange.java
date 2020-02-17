package stanislav.tun.novinomad.picasso.persistance.pojos;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

public class DateTimeRange {
    private LocalDateTime start, end;
    private int totalDays;
    private static DateTimeFormatter f = new DateTimeFormatterBuilder()
            .appendPattern("dd.MM.yyyy")
            .optionalStart()
            .appendPattern(" HH:mm")
            .optionalEnd()
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .toFormatter();

    public DateTimeRange(@NotNull LocalDateTime start, @NotNull LocalDateTime end) throws ValidationException {
        this.start = start;
        this.end = end;

        if(start.isAfter(end))
            throw new ValidationException("Wrong interval format. Start date cannot be larger than end date.");

        totalDays = (end.getDayOfYear() - start.getDayOfYear())+1;
    }

    /**
     * Single range
     * dd.mm.yyyy hh:mm - dd.mm.yyyy hh:mm
     * */
    public static DateTimeRange parseSingle(String range) throws ValidationException {
        var days = range.split(" - ");
        return new DateTimeRange(LocalDateTime.from(f.parse(days[0])), LocalDateTime.from(f.parse(days[1])));
    }

    /**
     * Multi range
     * dd.mm.yyyy hh:mm - dd.mm.yyyy hh:mm; dd.mm.yyyy hh:mm - dd.mm.yyyy hh:mm; ... dd.mm.yyyy hh:mm - dd.mm.yyyy hh:mm;
     * */
    public List<DateTimeRange> parseMulti(String range) throws ValidationException {
        var result = new ArrayList<DateTimeRange>();
        var ranges = range.trim().split(";");
        for (String r : ranges) {
            var arr = r.trim().split("-");
            result.add(new DateTimeRange(LocalDateTime.parse(arr[0]), LocalDateTime.parse(arr[1])));
        }
        return result;
    }

    public boolean overlaps(LocalDateTime from, LocalDateTime to){
        return ( (start.isBefore(to) || start.isEqual(to))&&(end.isAfter(from) || end.isEqual(from)) );
    }

    public boolean overlaps(DateTimeRange interval){
        return ( (start.isBefore(interval.getEnd()) || start.isEqual(interval.getEnd()))&&(end.isAfter(interval.getStart()) || end.isEqual(interval.getStart())) );
    }

    /**
     * used for front calendar display already attached specific days.
     * */
    @Override
    public String toString() {
        return f.format(start) + " - "+f.format(end);
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }
}
