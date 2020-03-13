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

    @Query("select t from tours as t where ( extract(MONTH FROM t.startDate) = :month and extract(YEAR from t.startDate) = :year)" +
                    "or (extract(month from t.endDate) = :month and extract(YEAR from t.endDate) = :year) order by t.startDate asc")
    Collection<Tour> findToursByMonthAndYear(@Param("month") int month, @Param("year") int year);

    @Query("select t from tours as t " +
            "join t.drivers as d " +
            "where d.id = :driverId " +
            "and ( extract(MONTH FROM t.startDate) = :month and extract(YEAR from t.startDate) = :year )" +
            "or (extract(month from t.endDate) = :month and extract(YEAR from t.endDate) = :year) order by t.startDate asc")
    Collection<Tour> findToursByMonthAndYearAndDriver(@Param("month") int month, @Param("year") int year, @Param("driverId") long driverId);

//    @Query("select distinct t from tours as t " +
//            "join t.guides as d " +
//            "where d.id = :guideId " +
//            "and ( extract(MONTH FROM t.startDate) = :month and extract(YEAR from t.startDate) = :year)" +
//            "or (extract(month from t.endDate) = :month and extract(YEAR from t.endDate) = :year) order by t.startDate asc")
@Query(value = "select t.* from tours as t join tours_guides as tg on tg.tour_id = t.id and tg.guide_id = :guideId where (extract(MONTH from t.start_date) = :month and extract(YEAR from t.start_date) = :year) or (extract(MONTH from t.end_date) = :month and extract(YEAR from t.end_date) = :year) order by t.start_date asc",
        nativeQuery = true)
    Collection<Tour> findToursByMonthAndYearAndGuide(@Param("month") int month, @Param("year") int year, @Param("guideId") long guideId);

    @Query("select t from tours as t where extract(YEAR from t.startDate) = :year or extract(YEAR from t.endDate) = :year order by t.startDate asc")
    Collection<Tour> findToursByYear(@Param("year") int year);


}
