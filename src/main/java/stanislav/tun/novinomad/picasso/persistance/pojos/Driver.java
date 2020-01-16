package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.Cascade;
import org.joda.time.Interval;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Indexed
@Entity(name = "drivers")
@JsonRootName(value = "driver")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "driverId")
public class Driver implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "driver_id")
    private long driverId;

    @Column
    @JsonIgnore
    private String lastName;

    @Column
    @JsonIgnore
    private String firstName;

    @Column
    @JsonIgnore
    private String middleName;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "drivers")
    @JsonBackReference
    //@Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Tour> tours = new HashSet<>();

    /* todo: learn. Sources:
    * https://thoughts-on-java.org/map-association-java-util-map/
    * https://stackoverflow.com/questions/21096412/getting-list-of-lists-in-hibernate => https://docs.jboss.org/hibernate/orm/3.3/api/org/hibernate/transform/ResultTransformer.html
    *
    * */
//    @Column(name = "intervals")
//    @ElementCollection(targetClass = Interval.class, fetch = FetchType.LAZY)
//    private List<Interval> intervals = new ArrayList<>();
//    @MapKey(name = "id")
//    private Map<Tour, Intervals> driverIntervals = new HashMap<>();
    public Driver() {

    }

    public Driver(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }



    // getters and setters
    public Set<Tour> getTours() {
        return tours;
    }

    public void setTours(Set<Tour> tours) {
        this.tours = tours;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

//    public List<Interval> getIntervals() {
//        return intervals;
//    }
//
//    public void setIntervals(List<Interval> intervals) {
//        this.intervals = intervals;
//    }

    public String getMiddleName() {
        if(middleName == null)
            return "";

        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFullName() {
        return String.format("%s %s %s", firstName == null ? "" : firstName, middleName == null ? "" : middleName, lastName == null ? "" : lastName);
    }


}
