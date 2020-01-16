package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.fasterxml.jackson.annotation.JsonRootName;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonRootName(value = "interval")
public class MyInterval {
    private LocalDateTime start, end;
    private int totalIntervalDays;

    public MyInterval(@NotNull LocalDateTime start, @NotNull LocalDateTime end) {
        this.start = start;
        this.end = end;
        totalIntervalDays = end.getDayOfYear() - start.getDayOfYear();
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
