package stanislav.tun.novinomad.picasso.persistance.repositories;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface ITourRepo extends JpaRepository<Tour, Long> {

    @Override
    List<Tour> findAll();

    Tour findByTittle(String tittle);

    @Query("select t from tours t where (extract(MONTH FROM t.startDate) = :month or extract(month from t.endDate) = :month) " +
            "and (extract(YEAR from t.startDate) = :year or extract(YEAR from t.endDate) = :year) order by t.startDate asc")
    Collection<Tour> findToursByMonthAndYear(@Param("month") int month, @Param("year") int year);

}
