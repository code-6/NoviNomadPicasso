package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

public class Guide extends Driver {
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "guides")
    @JsonBackReference
    @Fetch(FetchMode.JOIN)
    //@Cascade(org.hibernate.annotations.CascadeType.ALL)
    protected Set<Tour> tours = new HashSet<>();
}
