package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.io.Serializable;

@Indexed
@Entity(name = "drivers")
@JsonRootName(value = "driver")
public class Driver implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "driver_id")
    private long driverId;

    @Column
    private String lastName;

    @Column
    private String firstName;

    @Column
    private String middleName;

//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Set<Tour> tours = new HashSet<>();

    public Driver() {
        System.out.println("Start create new driver");
    }

    public Driver(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

//    public Set<Tour> getTours() {
//        return tours;
//    }
//
//    public void setTours(Set<Tour> tours) {
//        this.tours = tours;
//    }

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

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFullName() {
        return String.format("%s %s %s", firstName, middleName, lastName);
    }


}
