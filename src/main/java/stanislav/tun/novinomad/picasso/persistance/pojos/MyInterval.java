package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.validation.constraints.NotNull;
import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonRootName(value = "interval")
public class MyInterval {
    @JsonProperty
    private LocalDateTime start, end;
    @JsonProperty
    private int totalIntervalDays;

    public MyInterval(@NotNull LocalDateTime start, @NotNull LocalDateTime end) throws ValidationException {
        this.start = start;
        this.end = end;
        if(start.isAfter(end))
            throw new ValidationException("Wrong interval format. Start date cannot be larger than end date.");
        totalIntervalDays = end.getDayOfYear() - start.getDayOfYear();
    }

    /**
     * @param intervals - each interval should be in next format: {start date} -- {end date} ;
     * Where '--' double minus is separator for start and end date, and semicolon is separator for each interval.
     * @return List of interval objects.
     * */
    //@JsonProperty("intervals")
    public static List<MyInterval> parse(String intervals) throws ValidationException {
        intervals = intervals.trim();
        var list = new ArrayList<MyInterval>();
        // store {start date} -- {end date}
        var i = intervals.split(";");
        for (String date : i) {
            // start and end dates
            var di = date.split("--");
            list.add(new MyInterval(LocalDateTime.parse(di[0]), LocalDateTime.parse(di[1]) ));
        }
        return list;
    }

    public boolean overlaps(LocalDateTime from, LocalDateTime to){
        return ( (start.isBefore(to) || start.isEqual(to))&&(end.isAfter(from) || end.isEqual(from)) );
    }

    public boolean overlaps(MyInterval interval){
        return ( (start.isBefore(interval.getEnd()) || start.isEqual(interval.getEnd()))&&(end.isAfter(interval.getStart()) || end.isEqual(interval.getStart())) );
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public int getTotalIntervalDays() {
        return totalIntervalDays + 1;
    }

    /**
     * @return List of interval days. Example: if start date = 01.01.2020 and end date = 04.01.2020 then result will be
     * [01.01.2020, 02.01.2020, 03.01.2020, 04.01.2020]
     * */
    public List<LocalDateTime> toDaysList(){
        var list = new ArrayList<LocalDateTime>();
        list.add(start);
        var s = start;
        while(s.plusDays(1).isBefore(end)){
            s=s.plusDays(1);
            list.add(s);
        }
        list.add(end);
        return list;
    }

}