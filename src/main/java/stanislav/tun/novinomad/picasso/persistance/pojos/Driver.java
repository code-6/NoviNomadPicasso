package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Indexed
@Entity(name = "drivers")
@JsonRootName(value = "driver")
public class Driver implements Serializable {
    @Autowired
    @JsonIgnore
    @Transient
    ObjectMapper mapper;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "driver_id")
    private long id;

    @Column
    private String lastName;

    @Column
    private String firstName;

    @Column
    private String middleName;

//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Set<Tour> tours = new HashSet<>();

    public Driver() {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Driver:{" +
                    "id=" + id +
                    ", lastName=\"" + lastName + '\"' +
                    ", firstName=\"" + firstName + '\"' +
                    ", middleName=\"" + middleName + '\"' +
                    '}';
        }
    }


}
