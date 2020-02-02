package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.validation.constraints.NotNull;
import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//@JsonRootName(value = "interval")
public class MyInterval {
    //@JsonProperty
    private LocalDate start, end;
    //@JsonProperty
    private int totalDays;

    public MyInterval(@NotNull LocalDate start, @NotNull LocalDate end) throws ValidationException {
        this.start = start;
        this.end = end;
        if(start.isAfter(end))
            throw new ValidationException("Wrong interval format. Start date cannot be larger than end date.");
        totalDays = (end.getDayOfYear() - start.getDayOfYear())+1;
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
            try{
                var endDate = LocalDate.parse(di[1]);
                list.add(new MyInterval(LocalDate.parse(di[0]), endDate));
            }catch (Exception e){
                list.add(new MyInterval(LocalDate.parse(di[0]), LocalDate.parse(di[0])));
            }
        }
        return list;
    }

    public boolean overlaps(LocalDate from, LocalDate to){
        return ( (start.isBefore(to) || start.isEqual(to))&&(end.isAfter(from) || end.isEqual(from)) );
    }

    public boolean overlaps(MyInterval interval){
        return ( (start.isBefore(interval.getEnd()) || start.isEqual(interval.getEnd()))&&(end.isAfter(interval.getStart()) || end.isEqual(interval.getStart())) );
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public int getTotalDays() {
        return totalDays;
    }

    /**
     * @return List of interval days. Example: if start date = 01.01.2020 and end date = 04.01.2020 then result will be
     * [01.01.2020, 02.01.2020, 03.01.2020, 04.01.2020]
     * */
    public List<LocalDate> toDaysList(){
        var list = new ArrayList<LocalDate>();
        list.add(start);
        var s = start;
        while(s.plusDays(1).isBefore(end)){
            s=s.plusDays(1);
            list.add(s);
        }
        list.add(end);
        return list;
    }

    public String toDaysStringList(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        var list = toDaysList();
        var result = "";
        for( int i = 0; i < list.size(); i++ ){
            LocalDate date = list.get(i);
            if(i == list.size()-1)
                result += date.format(formatter);
            else
                result += date.format(formatter)+",";
        }
        return result;
    }

    @Override
    public String toString() {
        return "Interval{" +
                "start=" + start +
                ", end=" + end +
                ", totalDays=" + totalDays +
                '}';
    }

    // todo : create method that turns list of days to intervals;
}