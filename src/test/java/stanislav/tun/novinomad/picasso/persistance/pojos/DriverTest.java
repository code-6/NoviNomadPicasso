package stanislav.tun.novinomad.picasso.persistance.pojos;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import stanislav.tun.novinomad.picasso.NovinomadPicassoApp;
import stanislav.tun.novinomad.picasso.persistance.services.DriverService;
import stanislav.tun.novinomad.picasso.persistance.services.TourService;

import java.util.ArrayList;


@SpringBootTest
@ExtendWith(SpringExtension.class)
//@RunWith(SpringRunner.class)
@ContextConfiguration
class DriverTest {

    @Autowired
    DriverService driverService;

    @Autowired
    TourService tourService;

    Driver driver = new Driver("Test", "Test");
    Tour tour = new Tour();

    @Before
    void setUp() {

        driverService.createOrUpdateDriver(driver);

        tour.setTittle("Tour 1");
        tour.setStartDate(new LocalDateTime());
        tour.setEndDate(new LocalDateTime(2020, 01, 20, 10, 00));
        tour.addDriver(java.util.Optional.ofNullable(driver));

        tourService.createOrUpdateTour(tour);
    }

//    @org.junit.jupiter.api.Test
//    void addParticipateDate() {
//        driver.addParticipateDate(tour, new Interval(new DateTime(), new DateTime(2020, 01, 20, 10, 00)));
//        driverService.createOrUpdateDriver(driver);
//    }
//
//    @org.junit.jupiter.api.Test
//    void testAddParticipateDate() {
//        var intervals = new ArrayList<Interval>();
//        intervals.add(new Interval(new DateTime(), new DateTime(2020, 01, 20, 10, 00)));
//        intervals.add(new Interval(new DateTime(2020, 01, 20, 10, 00),
//                new DateTime(2020, 01, 29, 10, 00)));
//        driver.addParticipateDate(tour, intervals);
//        driverService.createOrUpdateDriver(driver);
//    }
}