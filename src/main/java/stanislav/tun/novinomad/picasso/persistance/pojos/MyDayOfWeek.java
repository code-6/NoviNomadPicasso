package stanislav.tun.novinomad.picasso.persistance.pojos;

import java.time.DayOfWeek;

public class MyDayOfWeek {
    private DayOfWeek dayOfWeek;
    private int day;
    private String label;
    private int month;
    private int year;

    public MyDayOfWeek(DayOfWeek dayOfWeek, int day) {
        this.dayOfWeek = dayOfWeek;
        this.day = day;
        char[] chars = dayOfWeek.name().toCharArray();
        label = chars[0]+""+chars[1];
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public int getDay() {
        return day;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "MyDayOfWeek{" +
                "dayOfWeek=" + dayOfWeek.name() +
                ", day=" + day +
                ", label='" + label + '\'' +
                '}';
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
