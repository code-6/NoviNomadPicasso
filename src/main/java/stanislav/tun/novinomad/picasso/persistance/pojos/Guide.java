package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

// todo; fix inheritance bug
@Entity(name = "guides")
@JsonRootName(value = "guide")
public class Guide extends AbstractEntity {
    @Column
    @JsonIgnore
    protected String lastName;

    @Column
    @JsonIgnore
    protected String firstName;

    @Column
    @JsonIgnore
    protected String middleName;

    protected String fullName;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "guides")
    @JsonBackReference
    @Fetch(FetchMode.JOIN)
    //@Cascade(org.hibernate.annotations.CascadeType.ALL)
    protected Set<Tour> tours = new HashSet<>();

//    @OneToMany( mappedBy = "guide", orphanRemoval = true)
//    @Fetch(FetchMode.JOIN)
//    private Set<GuideTourIntervals> guideTourIntervals = new HashSet<GuideTourIntervals>();

    public Guide() {

    }

    public Guide(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Set<Tour> getTours() {
        return tours;
    }

    public void setTours(Set<Tour> tours) {
        this.tours = tours;
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
        if (middleName == null)
            return "";

        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFullName() {
        return String.format("%s %s %s", firstName == null ? "" : firstName, middleName == null ? "" : middleName, lastName == null ? "" : lastName);
    }

//    public Set<GuideTourIntervals> getGuideTourIntervals() {
//        return guideTourIntervals;
//    }
//
//    public void setGuideTourIntervals(Set<GuideTourIntervals> guideTourIntervals) {
//        this.guideTourIntervals = guideTourIntervals;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Guide)) return false;
        if (!super.equals(o)) return false;
        Guide guide = (Guide) o;
        return fullName.equals(guide.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fullName);
    }
}
